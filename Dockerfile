FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/tracker-0.0.1-SNAPSHOT.jar tracker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "tracker.jar"]

docker run -v /c/Users/Administrator/Desktop/java/tracker/configs:/app/config `
  --env-file .env `
  -e SPRING_CONFIG_LOCATION=/app/config `
  -p 8080:8081 `
  tracker-test:0.0