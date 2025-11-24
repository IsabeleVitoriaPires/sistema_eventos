#!/bin/bash

#############################################
# JoinUp - Script de Inicialização Completo
# Inicia todos os microserviços do sistema
#############################################

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Diretório base
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOGS_DIR="${BASE_DIR}/logs"

# Criar diretório de logs
mkdir -p "${LOGS_DIR}"

# Arquivo para armazenar PIDs
PID_FILE="${BASE_DIR}/.service_pids"

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   JoinUp - Sistema de Ingressos${NC}"
echo -e "${CYAN}   Inicializando Microserviços${NC}"
echo -e "${CYAN}========================================${NC}\n"

#############################################
# Parar serviços anteriores
#############################################

echo -e "${YELLOW}► Parando processos Java anteriores...${NC}"

# Matar processos Java no WSL
java_pids=$(ps aux | grep java | grep -v grep | awk '{print $2}')
if [ -n "$java_pids" ]; then
    echo "$java_pids" | xargs kill -9 2>/dev/null && echo -e "${GREEN}✓ Processos Java no WSL finalizados${NC}"
else
    echo -e "${BLUE}ℹ Nenhum processo Java encontrado no WSL${NC}"
fi

# Matar processos Java no Windows (se aplicável)
if command -v taskkill.exe &> /dev/null; then
    taskkill.exe /F /IM java.exe /T 2>/dev/null && echo -e "${GREEN}✓ Processos Java no Windows finalizados${NC}" || true
fi

# Aguardar portas liberarem
sleep 3

echo

#############################################
# Funções Auxiliares
#############################################

check_port() {
    local port=$1
    if lsof -Pi :${port} -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        return 0  # Porta em uso
    else
        return 1  # Porta livre
    fi
}

wait_for_port() {
    local port=$1
    local service=$2
    local max_attempts=60
    local attempt=1

    echo -e "${YELLOW}Aguardando ${service} iniciar na porta ${port}...${NC}"

    while ! check_port ${port}; do
        if [ ${attempt} -eq ${max_attempts} ]; then
            echo -e "${RED}✗ Timeout aguardando ${service}${NC}"
            return 1
        fi
        sleep 2
        attempt=$((attempt + 1))
        echo -n "."
    done

    echo -e "\n${GREEN}✓ ${service} está rodando na porta ${port}${NC}"
    return 0
}

check_maven() {
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}✗ Maven não encontrado. Instale o Maven primeiro.${NC}"
        echo -e "${YELLOW}  Ubuntu/Debian: sudo apt install maven${NC}"
        echo -e "${YELLOW}  Fedora: sudo dnf install maven${NC}"
        exit 1
    fi
}

check_java() {
    if ! command -v java &> /dev/null; then
        echo -e "${RED}✗ Java não encontrado. Instale o Java 17 ou superior.${NC}"
        exit 1
    fi

    local java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "${java_version}" -lt 17 ]; then
        echo -e "${RED}✗ Java 17 ou superior necessário. Versão atual: ${java_version}${NC}"
        exit 1
    fi
}

start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    local wait_time=${4:-5}

    echo -e "\n${BLUE}► Iniciando ${service_name}...${NC}"

    # Verificar se porta já está em uso
    if check_port ${port}; then
        echo -e "${YELLOW}⚠ Porta ${port} já está em uso. ${service_name} pode já estar rodando.${NC}"
        return 0
    fi

    cd "${BASE_DIR}/${service_dir}"

    # Iniciar serviço em background
    nohup mvn spring-boot:run \
        > "${LOGS_DIR}/${service_name}.log" 2>&1 &

    local pid=$!
    echo "${service_name}:${pid}:${port}" >> "${PID_FILE}"

    echo -e "${GREEN}✓ ${service_name} iniciado (PID: ${pid})${NC}"
    echo -e "${CYAN}  Log: ${LOGS_DIR}/${service_name}.log${NC}"

    # Aguardar um pouco antes de verificar a porta
    sleep ${wait_time}
}

#############################################
# Verificações Iniciais
#############################################

echo -e "${BLUE}► Verificando pré-requisitos...${NC}"
check_java
check_maven
echo -e "${GREEN}✓ Pré-requisitos OK${NC}\n"

# Limpar arquivo de PIDs anterior
rm -f "${PID_FILE}"
touch "${PID_FILE}"

#############################################
# Compilar Serviços (se necessário)
#############################################

compile_if_needed() {
    local service_dir=$1
    local service_name=$2

    if [ ! -f "${BASE_DIR}/${service_dir}/target/"*.jar ]; then
        echo -e "${YELLOW}► Compilando ${service_name}...${NC}"
        cd "${BASE_DIR}/${service_dir}"
        mvn clean package -DskipTests -q
        echo -e "${GREEN}✓ ${service_name} compilado${NC}"
    fi
}

echo -e "${BLUE}► Verificando compilação dos serviços...${NC}"

# Perguntar se deseja recompilar
read -p "Deseja recompilar todos os serviços? [s/N]: " -n 1 -r
echo
if [[ $REPLY =~ ^[SsYy]$ ]]; then
    echo -e "${YELLOW}Compilando todos os serviços...${NC}"

    for service in service-discovery auth-service event-service ticket-service gateway-service; do
        echo -e "${BLUE}  Compilando ${service}...${NC}"
        cd "${BASE_DIR}/${service}"
        mvn clean package -DskipTests
    done

    echo -e "${GREEN}✓ Todos os serviços compilados${NC}\n"
else
    compile_if_needed "service-discovery" "Service Discovery"
    compile_if_needed "auth-service" "Auth Service"
    compile_if_needed "event-service" "Event Service"
    compile_if_needed "ticket-service" "Ticket Service"
    compile_if_needed "gateway-service" "Gateway Service"
    echo
fi

#############################################
# Iniciar Serviços em Ordem
#############################################

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN}   Iniciando Serviços${NC}"
echo -e "${CYAN}========================================${NC}\n"

# 1. Service Discovery (Eureka) - PRIMEIRO
start_service "service-discovery" "service-discovery" 8761 10
wait_for_port 8761 "Eureka Server"

echo -e "\n${YELLOW}Aguardando Eureka Server estabilizar...${NC}"
sleep 10

# 2. Auth Service
start_service "auth-service" "auth-service" 8084 10

# 3. Event Service
start_service "event-service" "event-service" 8083 10

# 4. Ticket Service
start_service "ticket-service" "ticket-service" 8085 10

# Aguardar serviços se registrarem no Eureka
echo -e "\n${YELLOW}Aguardando serviços se registrarem no Eureka...${NC}"
sleep 15

# 5. Gateway - POR ÚLTIMO
start_service "gateway-service" "gateway-service" 8080 10
wait_for_port 8080 "Gateway"

#############################################
# Verificar Status dos Serviços
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${CYAN}   Verificando Status dos Serviços${NC}"
echo -e "${CYAN}========================================${NC}\n"

sleep 5

check_service_health() {
    local name=$1
    local url=$2

    if curl -s -f "${url}" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ ${name}: UP${NC}"
        return 0
    else
        echo -e "${RED}✗ ${name}: DOWN${NC}"
        return 1
    fi
}

check_service_health "Eureka Server" "http://localhost:8761/actuator/health"
check_service_health "Gateway" "http://localhost:8080/actuator/health"

# Tentar chamar endpoints através do gateway
echo -e "\n${BLUE}► Testando endpoints através do Gateway:${NC}"

if curl -s "http://localhost:8080/api/events" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Event Service: Acessível via Gateway${NC}"
else
    echo -e "${YELLOW}⚠ Event Service: Aguarde mais alguns segundos...${NC}"
fi

#############################################
# Resumo Final
#############################################

echo -e "\n${CYAN}========================================${NC}"
echo -e "${CYAN}   Sistema Iniciado com Sucesso!${NC}"
echo -e "${CYAN}========================================${NC}\n"

echo -e "${GREEN}Serviços rodando:${NC}"
echo -e "  ${BLUE}►${NC} Eureka Server:    http://localhost:8761"
echo -e "  ${BLUE}►${NC} Gateway:          http://localhost:8080"
echo -e "  ${BLUE}►${NC} Auth Service:     http://localhost:8084"
echo -e "  ${BLUE}►${NC} Event Service:    http://localhost:8083"
echo -e "  ${BLUE}►${NC} Ticket Service:   http://localhost:8085"

echo -e "\n${GREEN}Endpoints principais:${NC}"
echo -e "  ${BLUE}►${NC} Listar eventos:   http://localhost:8080/api/events"
echo -e "  ${BLUE}►${NC} Registrar:        http://localhost:8080/api/auth/register"
echo -e "  ${BLUE}►${NC} Login:            http://localhost:8080/api/auth/login"

echo -e "\n${GREEN}Frontend:${NC}"
echo -e "  ${BLUE}►${NC} Abra: ${BASE_DIR}/frontend/login.html"

echo -e "\n${YELLOW}Logs dos serviços:${NC}"
echo -e "  ${BLUE}►${NC} ${LOGS_DIR}/"

echo -e "\n${YELLOW}Para parar todos os serviços:${NC}"
echo -e "  ${BLUE}►${NC} ./stop-all.sh"

echo -e "\n${YELLOW}Para verificar status:${NC}"
echo -e "  ${BLUE}►${NC} ./check-health.sh"

echo -e "\n${GREEN}PIDs salvos em: ${PID_FILE}${NC}\n"

# Mostrar PIDs
echo -e "${CYAN}Processos em execução:${NC}"
while IFS=: read -r service pid port; do
    if ps -p ${pid} > /dev/null 2>&1; then
        echo -e "  ${GREEN}✓${NC} ${service} (PID: ${pid}, Porta: ${port})"
    else
        echo -e "  ${RED}✗${NC} ${service} (PID: ${pid}) - Processo não encontrado"
    fi
done < "${PID_FILE}"

echo -e "\n${CYAN}========================================${NC}\n"
