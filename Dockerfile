# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copiar archivos de Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Dar permisos al wrapper
RUN chmod +x ./gradlew

# Descargar dependencias (cacheado si no cambia build.gradle)
RUN ./gradlew dependencies --no-daemon

# Copiar código fuente y compilar
COPY src src
RUN ./gradlew bootJar --no-daemon

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]