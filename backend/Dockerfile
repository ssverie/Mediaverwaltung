# ============================================================================
# Multi-Stage Dockerfile for MediaVerwaltung Spring Boot App
# ============================================================================
# Stage 1: Build
# Stage 2: Runtime
# ============================================================================

# ============================================================================
# STAGE 1: BUILD
# ============================================================================
FROM eclipse-temurin:21-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for layer caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (cached if pom.xml unchanged)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

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