FROM openjdk:22-jdk

# dobavit maven build and volumes

WORKDIR /app

EXPOSE 8080

COPY target/university-cms-0.0.1-SNAPSHOT.jar app/app.jar

ENTRYPOINT ["java", "-jar", "app/app.jar"]