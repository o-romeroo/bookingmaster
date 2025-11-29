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
COPY --from=build /app/target/bookingmaster-*.jar app.jar

# Variáveis de ambiente padrão
ENV DATABASE_URL=jdbc:mariadb://db:3306/bmdb?createDatabaseIfNotExist=true
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=root
ENV PORT=8080

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]