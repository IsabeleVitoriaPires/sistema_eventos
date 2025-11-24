#!/bin/bash

#############################################
# JoinUp - Script de Verificação de Status
# Verifica saúde de todos os microserviços
#############################################

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
NC='\033[0m' # No Color

# Diretório base
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="${BASE_DIR}/.service_pids"

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   JoinUp - Status do Sistema${NC}"
echo -e "${CYAN}========================================${NC}\n"

#############################################
# Verificar Processos
#############################################

echo -e "${MAGENTA}► PROCESSOS EM EXECUÇÃO${NC}\n"

if [ -f "${PID_FILE}" ]; then
    while IFS=: read -r service pid port; do
        if ps -p ${pid} > /dev/null 2>&1; then
            # Calcular uso de memória
            mem=$(ps -o rss= -p ${pid} 2>/dev/null | awk '{printf "%.1f MB", $1/1024}')
            cpu=$(ps -o %cpu= -p ${pid} 2>/dev/null | xargs)

            echo -e "${GREEN}✓${NC} ${service}"
            echo -e "  PID:    ${pid}"
            echo -e "  Porta:  ${port}"
            echo -e "  Memória: ${mem}"
            echo -e "  CPU:    ${cpu}%"
            echo ""
        else
            echo -e "${RED}✗${NC} ${service}"
            echo -e "  PID ${pid} não está rodando"
            echo ""
        fi
    done < "${PID_FILE}"
else
    echo -e "${YELLOW}Arquivo de PIDs não encontrado.${NC}"
    echo -e "${YELLOW}Verificando portas conhecidas...${NC}\n"

    check_process_on_port() {
        local port=$1
        local service_name=$2

        local pid=$(lsof -ti:${port} 2>/dev/null)
        if [ ! -z "${pid}" ]; then
            local mem=$(ps -o rss= -p ${pid} 2>/dev/null | awk '{printf "%.1f MB", $1/1024}')
            local cpu=$(ps -o %cpu= -p ${pid} 2>/dev/null | xargs)

            echo -e "${GREEN}✓${NC} ${service_name} (Porta ${port})"
            echo -e "  PID:    ${pid}"
            echo -e "  Memória: ${mem}"
            echo -e "  CPU:    ${cpu}%"
            echo ""
        else
            echo -e "${RED}✗${NC} ${service_name} (Porta ${port})"
            echo -e "  Não está rodando"
            echo ""
        fi
    }

    check_process_on_port 8761 "Eureka Server"
    check_process_on_port 8080 "Gateway"
    check_process_on_port 8084 "Auth Service"
    check_process_on_port 8083 "Event Service"
    check_process_on_port 8085 "Ticket Service"
fi

#############################################
# Verificar Health Endpoints
#############################################

echo -e "${CYAN}========================================${NC}"
echo -e "${MAGENTA}► STATUS DOS SERVIÇOS (Health Check)${NC}\n"

check_health() {
    local name=$1
    local url=$2

    echo -n "${name}: "

    response=$(curl -s -w "\n%{http_code}" "${url}" 2>/dev/null)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n-1)

    if [ "${http_code}" = "200" ]; then
        # Tentar extrair status do JSON
        status=$(echo "${body}" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "${status}" = "UP" ]; then
            echo -e "${GREEN}✓ UP${NC}"
        else
            echo -e "${YELLOW}⚠ ${status:-OK}${NC}"
        fi
    else
        echo -e "${RED}✗ DOWN (HTTP ${http_code})${NC}"
    fi
}

check_health "Eureka Server  " "http://localhost:8761/actuator/health"
check_health "Gateway        " "http://localhost:8080/actuator/health"

#############################################
# Verificar Serviços no Eureka
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${MAGENTA}► SERVIÇOS REGISTRADOS NO EUREKA${NC}\n"

eureka_response=$(curl -s "http://localhost:8761/eureka/apps" -H "Accept: application/json" 2>/dev/null)

if [ $? -eq 0 ] && [ ! -z "${eureka_response}" ]; then
    # Extrair aplicações registradas (simples parsing JSON)
    services=$(echo "${eureka_response}" | grep -o '"app":"[^"]*"' | cut -d'"' -f4 | sort -u)

    if [ ! -z "${services}" ]; then
        for service in ${services}; do
            instances=$(echo "${eureka_response}" | grep -c "\"app\":\"${service}\"")
            echo -e "${GREEN}✓${NC} ${service} (${instances} instância(s))"
        done
    else
        echo -e "${YELLOW}Nenhum serviço registrado${NC}"
    fi
else
    echo -e "${RED}✗ Não foi possível conectar ao Eureka${NC}"
fi

#############################################
# Testar Endpoints Principais
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${MAGENTA}► ENDPOINTS PRINCIPAIS (via Gateway)${NC}\n"

test_endpoint() {
    local name=$1
    local url=$2
    local method=${3:-GET}

    echo -n "${name}: "

    if [ "${method}" = "GET" ]; then
        http_code=$(curl -s -o /dev/null -w "%{http_code}" "${url}" 2>/dev/null)
    fi

    if [ "${http_code}" = "200" ]; then
        echo -e "${GREEN}✓ Acessível (HTTP ${http_code})${NC}"
    elif [ "${http_code}" = "401" ] || [ "${http_code}" = "403" ]; then
        echo -e "${YELLOW}⚠ Protegido (HTTP ${http_code})${NC}"
    else
        echo -e "${RED}✗ Não acessível (HTTP ${http_code})${NC}"
    fi
}

test_endpoint "Listar Eventos      " "http://localhost:8080/api/events"
test_endpoint "Event Service Direto" "http://localhost:8083/api/events"

#############################################
# Verificar Portas
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${MAGENTA}► STATUS DAS PORTAS${NC}\n"

check_port() {
    local port=$1
    local service=$2

    if lsof -Pi :${port} -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        echo -e "${GREEN}✓${NC} Porta ${port} (${service}): Em uso"
    else
        echo -e "${RED}✗${NC} Porta ${port} (${service}): Livre"
    fi
}

check_port 8761 "Eureka"
check_port 8080 "Gateway"
check_port 8084 "Auth Service"
check_port 8083 "Event Service"
check_port 8085 "Ticket Service"

#############################################
# Estatísticas de Sistema
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${MAGENTA}► ESTATÍSTICAS DO SISTEMA${NC}\n"

# Contar processos Java
java_count=$(ps aux | grep -c "[j]ava")
echo -e "Processos Java rodando: ${java_count}"

# Uso de memória total por Java
if [ ${java_count} -gt 0 ]; then
    total_mem=$(ps aux | grep "[j]ava" | awk '{sum+=$6} END {printf "%.1f MB", sum/1024}')
    echo -e "Memória total (Java):   ${total_mem}"
fi

# Uptime do sistema
uptime_info=$(uptime -p 2>/dev/null || uptime)
echo -e "Uptime do sistema:      ${uptime_info}"

#############################################
# Resumo e Dicas
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${MAGENTA}► DICAS${NC}\n"

# Verificar se todos os serviços estão UP
all_up=true
for port in 8761 8080 8084 8083 8085; do
    if ! lsof -Pi :${port} -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        all_up=false
        break
    fi
done

if [ "${all_up}" = true ]; then
    echo -e "${GREEN}✓ Todos os serviços estão rodando!${NC}\n"
    echo -e "${CYAN}Acesse:${NC}"
    echo -e "  • Eureka Dashboard: ${BLUE}http://localhost:8761${NC}"
    echo -e "  • API Gateway:      ${BLUE}http://localhost:8080${NC}"
    echo -e "  • Frontend:         ${BLUE}${BASE_DIR}/frontend/login.html${NC}"
else
    echo -e "${YELLOW}⚠ Alguns serviços não estão rodando${NC}\n"
    echo -e "${CYAN}Para iniciar:${NC}"
    echo -e "  • Todos os serviços: ${BLUE}./start-all.sh${NC}"
fi

echo -e "\n${CYAN}Outros comandos:${NC}"
echo -e "  • Parar tudo:        ${BLUE}./stop-all.sh${NC}"
echo -e "  • Ver logs:          ${BLUE}tail -f logs/<service>.log${NC}"

echo -e "\n${CYAN}========================================${NC}\n"
