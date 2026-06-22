# Paso 1: Usar una imagen oficial de Java 21 (la misma versión con la que compila tu Maven)
FROM eclipse-temurin:21-jdk-alpine

# Paso 2: Crear una carpeta dentro del contenedor para guardar la app
WORKDIR /app

# Paso 3: Copiar el archivo .jar que genera Maven dentro del contenedor
COPY target/*.jar app.jar

# Paso 4: El comando que ejecutará Docker para encender tu aplicación de Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]