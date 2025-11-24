@echo off
echo ====================================================================================================
echo COMPILANDO TODOS OS SERVICOS DO JOINUP
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
echo [1/5] Compilando Service Discovery (Eureka)...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\service-discovery"
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERRO ao compilar Service Discovery!
    pause
    exit /b 1
)

echo.
echo [2/5] Compilando Auth Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\auth-service"
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERRO ao compilar Auth Service!
    pause
    exit /b 1
)

echo.
echo [3/5] Compilando Event Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\event-service"
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERRO ao compilar Event Service!
    pause
    exit /b 1
)

echo.
echo [4/5] Compilando Ticket Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\ticket-service"
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERRO ao compilar Ticket Service!
    pause
    exit /b 1
)

echo.
echo [5/5] Compilando Gateway Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\gateway-service"
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ERRO ao compilar Gateway Service!
    pause
    exit /b 1
)

echo.
echo ====================================================================================================
echo TODOS OS SERVICOS FORAM COMPILADOS COM SUCESSO!
echo ====================================================================================================
echo.
echo Agora voce pode executar: INICIAR_TODOS_SERVICOS.bat
echo.
pause
