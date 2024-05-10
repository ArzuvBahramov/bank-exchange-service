FROM openjdk:17-slim as deps

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN set -xe \
    && apt-get update && apt-get install -y dos2unix \
    && dos2unix gradlew \
    && find ./ -name "*.java" | xargs dos2unix

COPY src src

RUN ./gradlew build -x test

FROM openjdk:17-slim

WORKDIR /app

COPY --from=deps /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
