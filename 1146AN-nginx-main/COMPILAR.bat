@echo off
setlocal enabledelayedexpansion

echo ====================================================================================================
echo COMPILANDO JOINUP - MODO GARANTIDO
echo ====================================================================================================
echo.

REM Ir para a pasta raiz do projeto
cd /d "%~dp0"

echo Diretorio atual: %CD%
echo.

REM Verificar Java
echo Verificando Java...
java -version 2>nul
if errorlevel 1 (
    echo.
    echo [ERRO] Java nao encontrado!
    echo.
    echo Por favor, instale o JDK 17:
    echo https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    echo.
    pause
    exit /b 1
)

echo Java OK!
echo.

REM Compilar Service Discovery
echo ====================================================================================================
echo [1/5] Compilando SERVICE DISCOVERY...
echo ====================================================================================================
cd service-discovery
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo ERRO: mvnw.cmd nao encontrado em service-discovery
    cd ..
    pause
    exit /b 1
)
if errorlevel 1 (
    echo ERRO ao compilar Service Discovery!
    cd ..
    pause
    exit /b 1
)
cd ..
echo Service Discovery OK!
echo.

REM Compilar Auth Service
echo ====================================================================================================
echo [2/5] Compilando AUTH SERVICE...
echo ====================================================================================================
cd auth-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo ERRO: mvnw.cmd nao encontrado em auth-service
    cd ..
    pause
    exit /b 1
)
if errorlevel 1 (
    echo ERRO ao compilar Auth Service!
    cd ..
    pause
    exit /b 1
)
cd ..
echo Auth Service OK!
echo.

REM Compilar Event Service
echo ====================================================================================================
echo [3/5] Compilando EVENT SERVICE...
echo ====================================================================================================
cd event-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo ERRO: mvnw.cmd nao encontrado em event-service
    cd ..
    pause
    exit /b 1
)
if errorlevel 1 (
    echo ERRO ao compilar Event Service!
    cd ..
    pause
    exit /b 1
)
cd ..
echo Event Service OK!
echo.

REM Compilar Ticket Service
echo ====================================================================================================
echo [4/5] Compilando TICKET SERVICE...
echo ====================================================================================================
cd ticket-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo ERRO: mvnw.cmd nao encontrado em ticket-service
    cd ..
    pause
    exit /b 1
)
if errorlevel 1 (
    echo ERRO ao compilar Ticket Service!
    cd ..
    pause
    exit /b 1
)
cd ..
echo Ticket Service OK!
echo.

REM Compilar Gateway Service
echo ====================================================================================================
echo [5/5] Compilando GATEWAY SERVICE...
echo ====================================================================================================
cd gateway-service
if exist mvnw.cmd (
    call mvnw.cmd clean package -DskipTests
) else (
    echo ERRO: mvnw.cmd nao encontrado em gateway-service
    cd ..
    pause
    exit /b 1
)
if errorlevel 1 (
    echo ERRO ao compilar Gateway Service!
    cd ..
    pause
    exit /b 1
)
cd ..
echo Gateway Service OK!
echo.

echo ====================================================================================================
echo SUCESSO! TODOS OS SERVICOS FORAM COMPILADOS!
echo ====================================================================================================
echo.
echo Agora execute: INICIAR_TODOS_SERVICOS.bat
echo.
pause
