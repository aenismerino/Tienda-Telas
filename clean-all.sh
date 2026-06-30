#!/bin/bash

services=(
  "api-gateway"
  "auth-service"
  "envio-service"
  "carrito-service"
  "inventario-service"
  "pago-service"
  "pedido-service"
  "notificaciones-service"
  "support-service"
)

for service in "${services[@]}"; do
  echo "----------------------------------------"
  echo "Cleaning $service..."
  echo "----------------------------------------"
  if [ -d "$service" ]; then
    cd "$service"
    # Ensure the Maven wrapper is executable
    chmod +x mvnw 2>/dev/null || true
    ./mvnw clean
    cd ..
  else
    echo "Warning: Directory $service not found."
  fi
done
