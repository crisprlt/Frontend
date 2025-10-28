@echo off
echo ================================================
echo   MONITOREANDO RENDIMIENTO DE CONCURRENCIA
echo ================================================
echo.
echo Presiona Ctrl+C para detener
echo.
echo Ejemplo de logs que veras:
echo   D/CONCURRENCY: Iniciando login concurrente...
echo   D/CONCURRENCY: Token guardado en: 15ms
echo   D/CONCURRENCY: Login completado en: 1034ms
echo   D/CONCURRENCY: Tiempo total de login: 1050ms
echo.
adb logcat -s CONCURRENCY:D CONCURRENCY:I CONCURRENCY:W CONCURRENCY:E
