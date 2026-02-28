FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/tracker.jar tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tracker.jar"]