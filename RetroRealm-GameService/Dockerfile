FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-GameService

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /RetroRealm-GameService

COPY --from=build /RetroRealm-GameService/target/RetroRealm-GameService-1.0.jar /RetroRealm-GameService/RetroRealm-GameService.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-GameService/RetroRealm-GameService.jar", "--server.port=${SERVER_PORT}"]