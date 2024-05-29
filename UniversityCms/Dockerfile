FROM openjdk:22-jdk

WORKDIR /app

EXPOSE 8080

COPY target/university-cms*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]