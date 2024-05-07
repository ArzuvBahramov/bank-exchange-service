FROM openjdk:17-slim as build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

COPY src src

ENV ESTCONTAINERS_HOST_OVERRIDE=host.docker.internal
STOPSIGNAL SIGKILL
VOLUME /var/run/docker.sock:/var/run/docker.sock

RUN ./gradlew build -x test

FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
