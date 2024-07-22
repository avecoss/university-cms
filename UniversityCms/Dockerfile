# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

# Copy the source files and the pom.xml file into the container
COPY src src
COPY pom.xml pom.xml

# Execute the Maven build command without running tests
RUN mvn clean package -DskipTests

# Stage 2: Creating the final image
FROM openjdk:21-ea-31-slim

# Define the JAR_FILE argument that will be used to copy the file
ARG JAR_FILE=target/university-cms*.jar

# Set the working directory
WORKDIR /app

# Open port 8080 for the application
EXPOSE 8080

# Copy the assembled JAR file from the previous step
COPY --from=build ${JAR_FILE} app.jar

# Define the application launch command
ENTRYPOINT ["java", "-jar", "app.jar"]