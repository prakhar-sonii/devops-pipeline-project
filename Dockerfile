# ============================================================
# Dockerfile — Student Management System
# INT332 DevOps Pipeline Project
# Demonstrates: Unit I (images, layers) + Unit II (Dockerfile)
# ============================================================

# ---- STAGE 1: BUILD STAGE ----
# Uses a full Maven + JDK image only for building
# This image is NOT shipped — it's discarded after build
# This is "multi-stage build" — reduces final image size drastically
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first — Docker caches this layer separately
# If pom.xml hasn't changed, Maven dependencies are NOT re-downloaded
# This is the "layer caching" optimization from Unit II
COPY pom.xml .

# Download all dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application — skip tests here (tests run in CI pipeline)
RUN mvn clean package -DskipTests

# ---- STAGE 2: RUNTIME STAGE ----
# Uses a minimal JRE-only image — no Maven, no build tools
# Much smaller and more secure than the build image
FROM eclipse-temurin:17-jre-alpine

# Add metadata labels (best practice from Unit II)
LABEL maintainer="INT332 DevOps Project"
LABEL version="1.0.0"
LABEL description="Student Management System REST API"

# Create a non-root user for security (DevSecOps best practice)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy ONLY the built JAR from the build stage — nothing else
COPY --from=build /app/target/student-app-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the application port
# EXPOSE documents the port — actual mapping is done in docker run or compose
EXPOSE 8080

# Health check — Docker will monitor if the app is alive
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/api/students/health || exit 1

# Entry point — runs the JAR
# Using exec form (JSON array) so signals are passed correctly to the JVM
ENTRYPOINT ["java", "-jar", "app.jar"]
