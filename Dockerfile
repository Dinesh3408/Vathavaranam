# Use OpenJDK 17 as base
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built jar into the container
COPY target/weather-api-0.0.1-SNAPSHOT.jar app.jar

# Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
