# Multi-stage Dockerfile for Spring Boot Application
# Stage 1: Build stage
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Copy gradle wrapper script
COPY gradlew ./
RUN chmod +x gradlew

# Copy build configuration files
COPY build.gradle settings.gradle ./

# Copy gradle wrapper configuration (if present)
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/ 2>/dev/null || \
    (mkdir -p gradle/wrapper && \
     echo "distributionBase=GRADLE_USER_HOME" > gradle/wrapper/gradle-wrapper.properties && \
     echo "distributionPath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties && \
     echo "distributionUrl=https\\://services.gradle.org/distributions/gradle-8.14.3-bin.zip" >> gradle/wrapper/gradle-wrapper.properties && \
     echo "networkTimeout=10000" >> gradle/wrapper/gradle-wrapper.properties && \
     echo "validateDistributionUrl=true" >> gradle/wrapper/gradle-wrapper.properties && \
     echo "zipStoreBase=GRADLE_USER_HOME" >> gradle/wrapper/gradle-wrapper.properties && \
     echo "zipStorePath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties)

# Copy gradle wrapper JAR if it exists, otherwise it will be downloaded
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/ 2>/dev/null || echo "Wrapper JAR will be downloaded"

# Copy source code
COPY src/ src/

# Build the application
# Since we're in a gradle image, we have gradle installed
# First, ensure wrapper is available (download if missing)
RUN if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then \
        echo "Downloading Gradle wrapper JAR..." && \
        gradle wrapper --gradle-version 8.14.3 --no-daemon; \
    fi && \
    ./gradlew clean bootJar -x test --no-daemon -x buildFrontend

# Verify JAR was created and show its name
RUN ls -la build/libs/ && \
    JAR_FILE=$(ls build/libs/*.jar | grep -v sources | grep -v javadoc | head -1) && \
    echo "JAR file: $JAR_FILE" && \
    cp "$JAR_FILE" app.jar

# Stage 2: Runtime stage (JRE only)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install wget for health check
RUN apk add --no-cache wget

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