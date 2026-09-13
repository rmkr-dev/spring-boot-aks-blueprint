# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package \
    && cp target/*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --no-create-home app \
    && chown -R app:app /app

COPY --from=build --chown=app:app /workspace/app.jar /app/app.jar

USER app
EXPOSE 8080

ENV SERVER_PORT=8080 \
    SPRING_APPLICATION_NAME=spring-boot-aks-blueprint \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
