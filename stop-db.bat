@echo off
set "SCRIPT_DIR=%~dp0"
set "PG_HOME=%SCRIPT_DIR%.tools\pgsql"
set "PGDATA=%SCRIPT_DIR%.tools\db_data"

echo [*] Forzando el apagado de PostgreSQL si quedo abierto en segundo plano...
"%PG_HOME%\bin\pg_ctl.exe" stop -D "%PGDATA%"
pause
