# Step 1: Build the JAR using Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven configuration and source code
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests to speed up)
RUN mvn clean package -DskipTests

# Step 2: Run the JAR using OpenJDK
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]

