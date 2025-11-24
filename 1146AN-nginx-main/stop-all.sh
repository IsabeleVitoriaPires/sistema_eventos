#!/bin/bash

#############################################
# JoinUp - Script de Parada Completo
# Para todos os microserviços do sistema
#############################################

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Diretório base
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PID_FILE="${BASE_DIR}/.service_pids"

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   JoinUp - Parando Microserviços${NC}"
echo -e "${CYAN}========================================${NC}\n"

#############################################
# Verificar se existe arquivo de PIDs
#############################################

if [ ! -f "${PID_FILE}" ]; then
    echo -e "${YELLOW}⚠ Arquivo de PIDs não encontrado.${NC}"
    echo -e "${YELLOW}  Tentando parar serviços por porta...${NC}\n"

    # Parar por porta conhecida
    for port in 8761 8080 8084 8083 8085; do
        pid=$(lsof -ti:${port} 2>/dev/null)
        if [ ! -z "${pid}" ]; then
            echo -e "${BLUE}► Parando serviço na porta ${port} (PID: ${pid})...${NC}"
            kill ${pid} 2>/dev/null
            sleep 2
            if ps -p ${pid} > /dev/null 2>&1; then
                echo -e "${YELLOW}  Forçando parada...${NC}"
                kill -9 ${pid} 2>/dev/null
            fi
            echo -e "${GREEN}✓ Serviço parado${NC}"
        fi
    done

    echo -e "\n${GREEN}Processo de parada concluído.${NC}\n"
    exit 0
fi

#############################################
# Parar Serviços Usando PIDs Salvos
#############################################

stopped_count=0
failed_count=0

while IFS=: read -r service pid port; do
    echo -e "${BLUE}► Parando ${service} (PID: ${pid})...${NC}"

    if ps -p ${pid} > /dev/null 2>&1; then
        # Tentar parada graceful primeiro
        kill ${pid} 2>/dev/null

        # Aguardar até 10 segundos
        for i in {1..10}; do
            if ! ps -p ${pid} > /dev/null 2>&1; then
                echo -e "${GREEN}✓ ${service} parado com sucesso${NC}"
                stopped_count=$((stopped_count + 1))
                break
            fi
            sleep 1
        done

        # Se ainda estiver rodando, forçar parada
        if ps -p ${pid} > /dev/null 2>&1; then
            echo -e "${YELLOW}  Forçando parada de ${service}...${NC}"
            kill -9 ${pid} 2>/dev/null
            sleep 1

            if ! ps -p ${pid} > /dev/null 2>&1; then
                echo -e "${GREEN}✓ ${service} parado forçadamente${NC}"
                stopped_count=$((stopped_count + 1))
            else
                echo -e "${RED}✗ Falha ao parar ${service}${NC}"
                failed_count=$((failed_count + 1))
            fi
        fi
    else
        echo -e "${YELLOW}⚠ ${service} não está rodando${NC}"
    fi

    echo ""
done < "${PID_FILE}"

#############################################
# Limpar Arquivo de PIDs
#############################################

rm -f "${PID_FILE}"
echo -e "${GREEN}✓ Arquivo de PIDs removido${NC}\n"

#############################################
# Verificar Portas
#############################################

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   Verificando Portas${NC}"
echo -e "${CYAN}========================================${NC}\n"

check_port_status() {
    local port=$1
    local service=$2

    if lsof -Pi :${port} -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        echo -e "${RED}✗ Porta ${port} (${service}): Ainda em uso${NC}"
        local pid=$(lsof -ti:${port})
        echo -e "${YELLOW}  PID: ${pid}${NC}"
        return 1
    else
        echo -e "${GREEN}✓ Porta ${port} (${service}): Livre${NC}"
        return 0
    fi
}

check_port_status 8761 "Eureka"
check_port_status 8080 "Gateway"
check_port_status 8084 "Auth Service"
check_port_status 8083 "Event Service"
check_port_status 8085 "Ticket Service"

#############################################
# Resumo Final
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${CYAN}   Resumo${NC}"
echo -e "${CYAN}========================================${NC}\n"

echo -e "${GREEN}Serviços parados: ${stopped_count}${NC}"

if [ ${failed_count} -gt 0 ]; then
    echo -e "${RED}Falhas: ${failed_count}${NC}"
    echo -e "\n${YELLOW}Dica: Use 'ps aux | grep java' para verificar processos Java${NC}"
    echo -e "${YELLOW}      Use 'kill -9 <PID>' para forçar parada${NC}\n"
else
    echo -e "\n${GREEN}Todos os serviços foram parados com sucesso!${NC}\n"
fi

echo -e "${YELLOW}Para iniciar novamente:${NC}"
echo -e "  ${BLUE}►${NC} ./start-all.sh\n"
