FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

COPY ./api/build/libs/api.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
