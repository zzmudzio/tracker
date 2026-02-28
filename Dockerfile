FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/tracker-0.0.1-SNAPSHOT.jar tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tracker.jar"]