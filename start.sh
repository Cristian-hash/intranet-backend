#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export JAVA_HOME="$SCRIPT_DIR/.tools/jdk-17.0.10+7"
export M2_HOME="$SCRIPT_DIR/.tools/apache-maven-3.9.6"
export PATH="$JAVA_HOME/bin:$M2_HOME/bin:$PATH"

echo "----------------------------------------"
echo "Configurando e Iniciando el Entorno..."
echo "JDK: $JAVA_HOME"
echo "Maven: $M2_HOME"
echo "----------------------------------------"

mvn -version
echo ""
echo "Iniciando Spring Boot Application..."
mvn clean spring-boot:run
