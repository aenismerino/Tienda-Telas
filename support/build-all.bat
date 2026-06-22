@echo off
echo ===================================================
echo COMPILANDO MICROSERVICIO DE SOPORTE Y ENTORNO DOCKER
echo ===================================================
call mvn clean package -DskipTests
echo Build de Maven completado con exito.
echo Construyendo imagenes en Docker...
docker compose build
echo Proceso finalizado. Puedes usar "docker compose up" para encenderlo.
pause