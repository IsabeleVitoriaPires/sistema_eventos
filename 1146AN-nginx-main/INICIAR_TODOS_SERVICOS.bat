@echo off
echo ====================================================================================================
echo INICIANDO TODOS OS SERVICOS DO JOINUP
echo ====================================================================================================
echo.

REM Configurar Java
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Verificar se Java esta configurado
java -version
if errorlevel 1 (
    echo ERRO: Java nao encontrado! Verifique se o JDK 17 esta instalado.
    pause
    exit /b 1
)

echo.
echo Verificando se todos os JARs foram compilados...
if not exist "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\service-discovery\target\service-discovery-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Service Discovery nao foi compilado!
    echo Execute primeiro: BUILD_ALL_SERVICES.bat
    pause
    exit /b 1
)
if not exist "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\auth-service\target\authservice-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Auth Service nao foi compilado!
    echo Execute primeiro: BUILD_ALL_SERVICES.bat
    pause
    exit /b 1
)
if not exist "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\event-service\target\event-service-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Event Service nao foi compilado!
    echo Execute primeiro: BUILD_ALL_SERVICES.bat
    pause
    exit /b 1
)
if not exist "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\ticket-service\target\ticket-service-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Ticket Service nao foi compilado!
    echo Execute primeiro: BUILD_ALL_SERVICES.bat
    pause
    exit /b 1
)
if not exist "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\gateway-service\target\gateway-service-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Gateway Service nao foi compilado!
    echo Execute primeiro: BUILD_ALL_SERVICES.bat
    pause
    exit /b 1
)

echo Todos os JARs encontrados! Iniciando servicos...
echo.
echo [1/5] Iniciando Service Discovery (Eureka) na porta 8761...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\service-discovery"
start "JoinUp - Service Discovery" cmd /c "java -jar target\service-discovery-0.0.1-SNAPSHOT.jar"

echo Aguardando 15 segundos para o Eureka inicializar...
timeout /t 15 /nobreak >nul

echo.
echo [2/5] Iniciando Auth Service na porta 8084...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\auth-service"
start "JoinUp - Auth Service" cmd /c "java -jar target\authservice-0.0.1-SNAPSHOT.jar"

echo.
echo [3/5] Iniciando Event Service na porta 8083...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\event-service"
start "JoinUp - Event Service" cmd /c "java -jar target\event-service-0.0.1-SNAPSHOT.jar"

echo.
echo [4/5] Iniciando Ticket Service na porta 8085...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\ticket-service"
start "JoinUp - Ticket Service" cmd /c "java -jar target\ticket-service-0.0.1-SNAPSHOT.jar"

echo.
echo [5/5] Iniciando Gateway Service na porta 8080...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\gateway-service"
start "JoinUp - Gateway" cmd /c "java -jar target\gateway-service-0.0.1-SNAPSHOT.jar"

echo.
echo ====================================================================================================
echo TODOS OS SERVICOS FORAM INICIADOS!
echo ====================================================================================================
echo.
echo 5 janelas CMD foram abertas, uma para cada servico.
echo.
echo Para verificar se tudo esta funcionando:
echo 1. Aguarde 30 segundos para todos os servicos iniciarem
echo 2. Acesse: http://localhost:8761
echo 3. Verifique se os 4 servicos aparecem registrados no Eureka
echo.
echo Para acessar o frontend:
echo - Abra: http://localhost:8080/index.html (via Gateway)
echo - Ou sirva os arquivos em frontend/ com um servidor HTTP
echo.
echo Para PARAR todos os servicos:
echo - Execute o script: PARAR_TODOS_SERVICOS.bat
echo - OU feche manualmente as 5 janelas CMD que foram abertas
echo.
echo ====================================================================================================
pause
