# Use Java 21 base image
FROM eclipse-temurin:21-jdk-alpine

# App jar name
ARG JAR_FILE=target/*.jar

# Copy jar
COPY ${JAR_FILE} app.jar

# Run app
ENTRYPOINT ["java", "-jar", "/app.jar"]