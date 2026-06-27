rem $env:DOCKER_BUILDKIT=0
rem docker rm -f $(docker ps -aq)
FOR /f %%i IN ('docker ps -aq') DO docker rm -f %%i
FOR /f %%i IN ('docker images -aq') DO docker rmi -f %%i

cd api-gateway
call .\mvnw clean package -DskipTests

cd ../auth-service
call .\mvnw clean package -DskipTests

cd ../carrito-service
call .\mvnw clean package -DskipTests

cd ../envio-service
call .\mvnw clean package -DskipTests

cd ../inventario-service
call .\mvnw clean package -DskipTests

cd ../pago-service
call .\mvnw clean package -DskipTests

cd ../pedido-service
call .\mvnw clean package -DskipTests

cd ../notificaciones-service
call .\mvnw clean package -DskipTests

cd ../support-service
call .\mvnw clean package -DskipTests
