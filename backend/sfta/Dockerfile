# Build stage: compile the Spring Boot app using Maven with JDK 21
FROM maven:3.9.4-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage: use lightweight Eclipse Temurin JRE 21 Alpine image to run the app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

