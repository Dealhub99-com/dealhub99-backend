# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn -DskipTests clean package -B

# Stage 2: Run the application (minimal Alpine JRE image)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/dealhub99-backend-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory
RUN mkdir -p /app/uploads

# Expose port
EXPOSE 8080

# JVM memory tuning for low-memory free hosting (256MB-512MB containers)
# -Xms64m  : start with 64MB heap
# -Xmx200m : limit max heap to 200MB (leaves room for JVM overhead)
# -XX:+UseSerialGC : serial GC uses less memory than parallel GC
# -XX:MaxMetaspaceSize=128m : cap metaspace
ENTRYPOINT ["java", \
  "-Xms64m", \
  "-Xmx200m", \
  "-XX:+UseSerialGC", \
  "-XX:MaxMetaspaceSize=128m", \
  "-XX:+TieredCompilation", \
  "-XX:TieredStopAtLevel=1", \
  "-jar", "app.jar"]
