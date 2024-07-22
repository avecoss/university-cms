# University Content Management System (UniversityCms)

UniversityCms is a comprehensive MVC project designed for managing university courses, students, groups, schedules and staff. Built using Spring Boot, it leverages a range of technologies to provide a robust and secure platform. The application is containerized using Docker and can be easily deployed using Docker Compose.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Swagger Documentation](#swagger-documentation)
- [Contributors](#contributors)

## Technologies Used
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap
- Flyway
- PostgreSQL
- Validation
- Lombok
- TestContainers
- Logback
- Docker

## Features
1. **Welcome Page:** Publicly accessible, providing an overview of the institution and necessary navigation links.
2. **Authentication and Authorization:**
   - Authenticated users can access specific sections like groups and courses ect.
   - Main page is accessible to all users.
   - Admin panel is restricted to administrators ("admin"), allowing them to delete groups, courses, and other entities. Also allows them to change roles to users.
   - Users with "staff" and "admin" roles can perform edit operations, while regular users have read-only access.
3. **Group Management:** View groups, brief descriptions, and lists of students. Links to student profiles.
4. **Course Management:** View courses, descriptions, and instructor details. Links to instructor profiles.
5. **Professor Management:** View teachers and the courses they teach. Links to course pages.
6. **Student Management:** Student profiles show their groups and courses. Links to their group and course pages.
7. **Schedule Management:** Displays course schedules with start and end times, group, course, and instructor details.
8. **CRUD Operations:** Role-dependent CRUD operations.
9. **User Profiles:** Users can view and edit their profile information.
10. **Pagination, Sorting, and Search:** Implemented across various sections for efficient data management.
11. **Security:** Basic Authentication. Passwords are encrypted using BCrypt.

## Getting Started
### Prerequisites
- Docker installed on your machine.

### Running the Application
1. Clone the repository:

    ```sh
    git clone https://github.com/avecoss/university-cms.git
    cd university-cms
    ```
2. Build and run the services using Docker Compose:

    ```sh
    docker-compose up --build
    ```
3. Access the application:
   - UniversityCms: http://localhost:8080
   - Adminer: http://localhost:8090

## Screenshots
### Home Page
<img width="1416" alt="homepage" src="https://github.com/user-attachments/assets/4285077a-08a7-45b5-b622-4ebaf5f2acf2">

### Admin panel
`ADMIN` role

<img width="1336" alt="adminpanel" src="https://github.com/user-attachments/assets/d545f69c-aaf5-4c4e-8969-fb2fdc90228e">

### Schedules
`ADMIN` role

<img width="1374" alt="schedules" src="https://github.com/user-attachments/assets/90c6a685-d932-4122-ac20-923c05b0b9d2">

### Lesson
`STAFF` role

<img width="1366" alt="lesson" src="https://github.com/user-attachments/assets/e5bd1ba5-e85f-49f0-9418-4cc690bdd7bb">

### Courses
`ADMIN` role

<img width="1348" alt="coursesAdmin" src="https://github.com/user-attachments/assets/695b6bb3-e8b7-451a-826a-9a8f00a318ab">

### Course
`STAFF` role

<img width="1359" alt="CourseStaff" src="https://github.com/user-attachments/assets/5774b261-2c3d-41f0-a7b5-4efb44fe0e9c">

### Courses
`USER` role

<img width="1418" alt="coursesUser" src="https://github.com/user-attachments/assets/d4f68961-5883-4d80-88e6-b3080edd0180">

## Configuration
### Docker Setup
The application uses Docker for containerization. The `docker-compose.yml` file defines the setup:

- **cms_service**: The main application service.
- **db_pg**: The PostgreSQL database service.
- **adminer**: A database management tool.
#### `docker-compose.yml`
```yml
name: u-cms

services:
    cms_service:
        image: university-cms:lasted
        build:
            context: ./UniversityCms
            dockerfile: Dockerfile
        restart: always
        ports:
            - "8080:8080"
        depends_on:
            - db_pg
        environment:
            - SPRING_DATASOURCE_URL=jdbc:postgresql://db_pg:5432/university_database
        container_name: "university-cms"

    db_pg:
        image: postgres:16-alpine
        restart: always
        environment:
            POSTGRES_DB: university_database
            POSTGRES_USER: your_username
            POSTGRES_PASSWORD: your_password
        ports:
            - "5433:5432"
        container_name: "db-pg"

    adminer:
        image: adminer
        restart: always
        ports:
            - "8090:8080"
        container_name: "adminer"
```
### Docker Setup
The `Dockerfile` is used to build the Docker image:
```dockerfile
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
```

### Application Configuration
Update the `application.yml` file with your PostgreSQL credentials and other configurations as needed.
#### `application.yml`
```yml
spring:
  application:
    name: UniversityCms

  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5433/university_database
    username: your_username
    password: your_password

  flyway:
    locations: db/migration
    baselineOnMigrate: true
    baselineVersion: 1
    baselineDescription: Base migration
    schemas:
      - public
      - university

  jpa:
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        jdbc:
          batch_size: 10
        order_updates: true
        order_inserts: true
```

### Security Configuration
**Security Configuration**: The `SecurityConfig` class configures Spring Security for role-based access control.

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

   private final PersonDetailsService personDetailsService;

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      return http
              .authorizeHttpRequests(authorize ->
                      authorize
                              .requestMatchers("/webjars/**", "/css/**", "/icons/**", "/img/**").permitAll()
                              .requestMatchers("/login", "/error", "/registration", "/home", "/courses").permitAll()
                              .requestMatchers(RegexRequestMatcher.regexMatcher("/courses/\\d+")).permitAll()
                              .requestMatchers("/admin/**").hasRole("ADMIN")
                              .anyRequest().authenticated())
              .formLogin(login ->
                      login
                              .loginPage("/login")
                              .defaultSuccessUrl("/home", true)
                              .failureUrl("/login?error"))
              .logout(logout ->
                      logout
                              .logoutSuccessUrl("/login?logout")
                              .deleteCookies("JSESSIONID")
                              .logoutSuccessHandler(logout.getLogoutSuccessHandler()))
              .build();
   }

   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(personDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
      return authProvider;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
```
## Contributors
- [avexcoss](https://github.com/avecoss)