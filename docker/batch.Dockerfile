FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

COPY ./batch/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]

