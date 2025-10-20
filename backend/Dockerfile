# ============================================================================
# Multi-Stage Dockerfile for MediaVerwaltung Spring Boot App
# ============================================================================
# Stage 1: Build
# Stage 2: Runtime
# ============================================================================

# ============================================================================
# STAGE 1: BUILD
# ============================================================================
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (for layer caching)
COPY pom.xml ./

# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests for faster build)
RUN mvn clean package -DskipTests

# ============================================================================
# STAGE 2: RUNTIME
# ============================================================================
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/mediaverwaltung-*.jar app.jar

# Expose port (Render uses PORT env variable)
EXPOSE 8080

# Environment variables (can be overridden by Render)
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]