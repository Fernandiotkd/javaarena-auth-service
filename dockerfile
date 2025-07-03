# Use an OpenJDK 21 slim base image for a reduced image size
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy your application's JAR file to the container
# Make sure to replace 'your-application-name.jar' with the actual name of your JAR
COPY target/java-arena-auth-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which your Spring Boot application listens (default 8080)
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
#docker build -t java-arena-auth-service .
#docker run -p 8080:8080 -e GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json -v C:\Users\FERNANDO\AppData\Roaming\gcloud\application_credentials.json:/app/credentials.json java-arena-auth-service