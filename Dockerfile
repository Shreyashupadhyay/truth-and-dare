# Multi-stage Dockerfile for Spring Boot Application
# Stage 1: Build stage
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and build files first (for better caching)
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build the application (skip tests, create executable JAR)
# Using --no-daemon for Docker builds
# Excluding frontend build for backend-only deployment
RUN ./gradlew clean bootJar -x test --no-daemon -x buildFrontend

# Verify JAR was created and show its name
RUN ls -la build/libs/ && \
    JAR_FILE=$(ls build/libs/*.jar | grep -v sources | grep -v javadoc | head -1) && \
    echo "JAR file: $JAR_FILE" && \
    cp "$JAR_FILE" app.jar

# Stage 2: Runtime stage (JRE only)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the JAR from build stage
COPY --from=build /app/app.jar app.jar

# Expose port (will be overridden by Render's PORT env var)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-8080}/api/health || exit 1

# Run the application
# Use PORT environment variable or default to 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]