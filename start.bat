@echo off
set "SCRIPT_DIR=%~dp0"
set "JAVA_HOME=%SCRIPT_DIR%.tools\jdk-17.0.10+7"
set "M2_HOME=%SCRIPT_DIR%.tools\apache-maven-3.9.6"
set "PG_HOME=%SCRIPT_DIR%.tools\pgsql"
set "PGDATA=%SCRIPT_DIR%.tools\db_data"
set "PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PG_HOME%\bin;%PATH%"

echo ----------------------------------------
echo Configurando el Entorno Completo
echo JDK: %JAVA_HOME%
echo DB Data: %PGDATA%
echo ----------------------------------------

IF NOT EXIST "%PGDATA%" (
    echo [!] Configurando la Base de Datos PostgreSQL por primera vez...
    "%PG_HOME%\bin\initdb.exe" -D "%PGDATA%" -U postgres -E UTF8

    echo [!] Iniciando PostgreSQL internamente...
    "%PG_HOME%\bin\pg_ctl.exe" -w start -D "%PGDATA%"

    echo [!] Configurando contrasena y creando la BBDD intranet_db...
    "%PG_HOME%\bin\psql.exe" -U postgres -c "ALTER USER postgres WITH PASSWORD 'password';"
    "%PG_HOME%\bin\createdb.exe" -U postgres intranet_db
    echo [OK] Base de datos estructurada.
) ELSE (
    echo [!] Iniciando PostgreSQL local...
    "%PG_HOME%\bin\pg_ctl.exe" -w start -D "%PGDATA%"
)

echo.
echo [*] Iniciando el Servidor Spring Boot...
call mvn clean spring-boot:run

echo.
echo [*] Servidor finalizado. Deteniendo BD...
"%PG_HOME%\bin\pg_ctl.exe" stop -D "%PGDATA%"
pause
