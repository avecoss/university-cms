FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean package

FROM openjdk:17-jdk-alpine

WORKDIR /app

EXPOSE 8081

COPY src/main/resources/data/first_names.txt /app/data/first_names.txt
COPY src/main/resources/data/last_names.txt /app/data/last_names.txt

COPY --from=build target/university-generator*.jar ./app.jar

ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]