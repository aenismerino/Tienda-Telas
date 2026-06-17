#!/bin/bash
echo "Iniciando todos los microservicios en Kali Linux..."

# Dar permisos de ejecución a los scripts de Maven por si acaso (suele pasar en Linux que se pierden al pasar por Windows)
chmod +x api-gateway/mvnw 2>/dev/null
chmod +x auth-service/mvnw 2>/dev/null
chmod +x envio-service/mvnw 2>/dev/null
chmod +x inventario/inventario/mvnw 2>/dev/null
chmod +x pago-service/mvnw 2>/dev/null
chmod +x pedido/pedido/mvnw 2>/dev/null

# Función para iniciar un microservicio en una nueva ventana
iniciar_servicio() {
    NOMBRE=$1
    DIRECTORIO=$2
    echo "Lanzando $NOMBRE..."
    
    # Kali Linux usa x-terminal-emulator como enlace simbólico estándar para su terminal gráfica (qterminal, xfce4-terminal, etc)
    if command -v x-terminal-emulator &> /dev/null; then
        x-terminal-emulator -e bash -c "cd \"$DIRECTORIO\" && ./mvnw spring-boot:run; echo ''; read -p 'Presiona Enter para cerrar esta ventana...'" &
    else
        # Fallback por si lo ejecutan sin entorno gráfico o no encuentra el emulador
        echo "Iniciando $NOMBRE en segundo plano (logs en $NOMBRE.log)..."
        (cd "$DIRECTORIO" && ./mvnw spring-boot:run > "../$NOMBRE.log" 2>&1) &
    fi
}

# Iniciando todos los microservicios de manera asíncrona
iniciar_servicio "API_Gateway" "api-gateway"
iniciar_servicio "Auth_Service" "auth-service"
iniciar_servicio "Envio_Service" "envio-service"
iniciar_servicio "Inventario_Service" "inventario/inventario"
iniciar_servicio "Pago_Service" "pago-service"
iniciar_servicio "Pedido_Service" "pedido/pedido"

echo "¡Todos los microservicios están inicializándose!"
echo "Revisa las nuevas ventanas de terminal."
