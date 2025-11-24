@echo off
echo ====================================================================================================
echo COMPILANDO SERVICOS DO JOINUP - MODO SIMPLES
echo ====================================================================================================
echo.
echo Este script compila os servicos um por vez.
echo Aguarde ate aparecer "BUILD SUCCESS" antes de fechar!
echo.
echo ====================================================================================================

REM Configurar Java
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo.
echo Qual servico deseja compilar?
echo.
echo 1 - Service Discovery (Eureka)
echo 2 - Auth Service
echo 3 - Event Service
echo 4 - Ticket Service
echo 5 - Gateway Service
echo 6 - TODOS (um por vez)
echo 0 - Sair
echo.

set /p opcao="Digite o numero: "

if "%opcao%"=="1" goto discovery
if "%opcao%"=="2" goto auth
if "%opcao%"=="3" goto event
if "%opcao%"=="4" goto ticket
if "%opcao%"=="5" goto gateway
if "%opcao%"=="6" goto todos
if "%opcao%"=="0" exit
goto fim

:discovery
echo.
echo Compilando Service Discovery...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\service-discovery"
mvnw.cmd clean package -DskipTests
pause
goto fim

:auth
echo.
echo Compilando Auth Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\auth-service"
mvnw.cmd clean package -DskipTests
pause
goto fim

:event
echo.
echo Compilando Event Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\event-service"
mvnw.cmd clean package -DskipTests
pause
goto fim

:ticket
echo.
echo Compilando Ticket Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\ticket-service"
mvnw.cmd clean package -DskipTests
pause
goto fim

:gateway
echo.
echo Compilando Gateway Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\gateway-service"
mvnw.cmd clean package -DskipTests
pause
goto fim

:todos
echo.
echo [1/5] Compilando Service Discovery...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\service-discovery"
mvnw.cmd clean package -DskipTests
if errorlevel 1 goto erro

echo.
echo [2/5] Compilando Auth Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\auth-service"
mvnw.cmd clean package -DskipTests
if errorlevel 1 goto erro

echo.
echo [3/5] Compilando Event Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\event-service"
mvnw.cmd clean package -DskipTests
if errorlevel 1 goto erro

echo.
echo [4/5] Compilando Ticket Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\ticket-service"
mvnw.cmd clean package -DskipTests
if errorlevel 1 goto erro

echo.
echo [5/5] Compilando Gateway Service...
cd /d "C:\Users\isabe\sistema_eventos\1146AN-nginx-main\gateway-service"
mvnw.cmd clean package -DskipTests
if errorlevel 1 goto erro

echo.
echo ====================================================================================================
echo TODOS OS SERVICOS COMPILADOS COM SUCESSO!
echo ====================================================================================================
pause
goto fim

:erro
echo.
echo ====================================================================================================
echo ERRO NA COMPILACAO!
echo ====================================================================================================
echo.
echo Verifique os logs acima para ver o erro.
echo.
pause
goto fim

:fim
