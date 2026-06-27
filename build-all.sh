#!/bin/bash

# Remove all docker containers and images if they exist
docker ps -aq | xargs -r docker rm -f
docker images -aq | xargs -r docker rmi -f

services=(
  "api-gateway"
  "auth-service"
  "carrito-service"
  "envio-service"
  "inventario-service"
  "pago-service"
  "pedido-service"
  "notificaciones-service"
  "support-service"
)

for service in "${services[@]}"; do
  echo "----------------------------------------"
  echo "Building $service..."
  echo "----------------------------------------"
  if [ -d "$service" ]; then
    cd "$service"
    # Ensure the Maven wrapper is executable
    chmod +x mvnw 2>/dev/null || true
    ./mvnw clean package -DskipTests
    cd ..
  else
    echo "Warning: Directory $service not found."
  fi
done
