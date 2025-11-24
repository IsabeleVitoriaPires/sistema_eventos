@echo off
echo =========================================
echo  Recompilando e Iniciando AUTH-SERVICE
echo =========================================

set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo.
echo [1/5] Parando todos os serviços Java...
taskkill /F /IM java.exe /T 2>nul
if %ERRORLEVEL%==0 (
    echo Serviços parados com sucesso.
) else (
    echo Nenhum serviço Java em execução.
)

echo.
echo [2/5] Aguardando 5 segundos para liberar os arquivos...
timeout /t 5 /nobreak >nul

echo.
echo [3/5] Recompilando auth-service...
cd "%~dp0auth-service"
call mvnw clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha ao compilar auth-service!
    pause
    exit /b 1
)

echo.
echo [4/5] Aguardando 3 segundos...
timeout /t 3 /nobreak >nul

echo.
echo [5/5] Iniciando todos os serviços...
cd "%~dp0"
call INICIAR_TODOS_SERVICOS.bat

echo.
echo =========================================
echo  Processo concluído!
echo  Aguarde 30 segundos para todos os
echo  serviços estarem prontos.
echo =========================================
