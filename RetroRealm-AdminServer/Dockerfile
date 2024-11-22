FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-AdminServer

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-AdminServer

COPY --from=build /RetroRealm-AdminServer/target/RetroRealm-AdminServer-1.0.jar /RetroRealm-AdminServer/RetroRealm-AdminServer.jar


EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-AdminServer/RetroRealm-AdminServer.jar", "--server.port=${SERVER_PORT}"]