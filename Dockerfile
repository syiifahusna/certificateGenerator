# Use a minimal Java 17 runtime base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the files into the container
COPY /files files

# Copy the JAR file into the container
COPY target/certificateGenerator-0.0.1-SNAPSHOT.jar certificateGenerator-0.0.1-SNAPSHOT.jar

# Expose port (optional, if your app listens on e.g. 8080)
EXPOSE 8081

# Command to run the app
ENTRYPOINT ["java", "-jar", "certificateGenerator-0.0.1-SNAPSHOT.jar"]