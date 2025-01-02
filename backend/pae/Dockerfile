# Step 1: Use a Maven image with Java 17 for building
FROM maven:3.8.4-openjdk-17 as build

# Step 2: Copy application source code
WORKDIR /app
COPY src ./src
COPY pom.xml .

# Step 3: Build the application
RUN mvn clean package -DskipTests

# Step 4: Use a lightweight Java 17 runtime for execution
FROM openjdk:17-jdk-alpine

# Step 5: Set environment variables (to be populated by Docker Compose)
ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""
ENV APP_PASSWORD=""

# Step 6: Copy the built JAR file
COPY --from=build /app/target/*.jar app.jar

# Step 7: Expose the application port
EXPOSE 8080

# Step 8: Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
