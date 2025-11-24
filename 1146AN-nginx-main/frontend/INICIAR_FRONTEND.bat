@echo off
echo ====================================================================================================
echo INICIANDO FRONTEND DO JOINUP
echo ====================================================================================================
echo.

REM Verificar se Python está instalado
python --version >nul 2>&1
if errorlevel 1 (
    echo Python nao encontrado!
    echo.
    echo SOLUCAO 1: Instale o Python
    echo Download: https://www.python.org/downloads/
    echo.
    echo SOLUCAO 2: Use outro navegador
    echo Alguns navegadores modernos permitem abrir HTML direto sem CORS issues.
    echo Tente com Microsoft Edge ou Firefox Developer Edition.
    echo.
    pause
    exit /b 1
)

echo Python encontrado!
echo.
echo Iniciando servidor HTTP na porta 3000...
echo.
echo ====================================================================================================
echo FRONTEND DISPONIVEL EM:
echo http://localhost:3000
echo ====================================================================================================
echo.
echo Abra o navegador e acesse:
echo   http://localhost:3000/login.html
echo.
echo Pressione Ctrl+C para parar o servidor
echo.
echo ====================================================================================================
echo.

python -m http.server 3000
