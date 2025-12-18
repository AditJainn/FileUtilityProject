# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build fat JAR
COPY src ./src
RUN mvn clean package -DskipTests


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the Spring Boot fat JAR
COPY --from=build /app/target/*.jar app.jar

# Internal container port (not exposed publicly)
EXPOSE 8080

# JVM tuned for containers
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75","-jar","app.jar"]
