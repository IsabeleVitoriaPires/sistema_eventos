@echo off
echo ====================================================================================================
echo INICIANDO SISTEMA COMPLETO DO JOINUP
echo ====================================================================================================
echo.
echo Este script vai:
echo 1. Verificar se os servicos foram compilados
echo 2. Iniciar todos os 5 servicos backend
echo 3. Iniciar o servidor do frontend
echo.
echo Pressione qualquer tecla para continuar...
pause >nul
echo.

REM Verificar se os JARs existem
echo Verificando se os servicos foram compilados...
if not exist "service-discovery\target\service-discovery-0.0.1-SNAPSHOT.jar" (
    echo ERRO: Servicos nao compilados!
    echo Execute primeiro: COMPILAR.bat
    pause
    exit /b 1
)

echo JARs encontrados! Continuando...
echo.

REM Iniciar backend
echo ====================================================================================================
echo INICIANDO BACKEND (5 servicos)
echo ====================================================================================================
echo.
start "" INICIAR_TODOS_SERVICOS.bat

echo Aguardando 10 segundos para o backend iniciar...
timeout /t 10 /nobreak >nul

REM Iniciar frontend
echo.
echo ====================================================================================================
echo INICIANDO FRONTEND
echo ====================================================================================================
echo.

cd frontend
start "" INICIAR_FRONTEND.bat

echo.
echo ====================================================================================================
echo SISTEMA COMPLETO INICIADO!
echo ====================================================================================================
echo.
echo Aguarde 60 segundos e depois acesse:
echo   http://localhost:3000/login.html
echo.
echo Para verificar os servicos backend:
echo   http://localhost:8761
echo.
pause
