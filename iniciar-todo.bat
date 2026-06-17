@echo off
echo Iniciando todos los microservicios...

start "API Gateway" cmd /k "cd api-gateway && mvnw clean spring-boot:run"
start "Auth Service" cmd /k "cd auth-service && mvnw clean spring-boot:run"
start "Envio Service" cmd /k "cd envio-service && mvnw clean spring-boot:run"
start "Inventario Service" cmd /k "cd inventario-service && mvnw clean spring-boot:run"
start "Pago Service" cmd /k "cd pago-service && mvnw clean spring-boot:run"
start "Pedido Service" cmd /k "cd pedido-service && mvnw clean spring-boot:run"

echo Microservicios iniciándose en nuevas ventanas!
