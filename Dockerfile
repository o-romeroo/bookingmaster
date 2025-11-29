# Dockerfile para BookingMaster API
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

# Instala curl para health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/bookingmaster-*.jar app.jar

# Variáveis de ambiente padrão (alinhadas com docker-compose.yml)
ENV DATABASE_URL=jdbc:mariadb://bookingmaster-db:3306/bmdb?createDatabaseIfNotExist=true
ENV DATABASE_USERNAME=bmuser
ENV DATABASE_PASSWORD=bmpassword
ENV PORT=8080

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]