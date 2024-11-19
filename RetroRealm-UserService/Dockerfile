FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-UserService

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-UserService

COPY --from=build /RetroRealm-UserService/target/RetroRealm-UserService-1.0.jar /RetroRealm-UserService/RetroRealm-UserService.jar

# EXPOSE 8761

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-UserService/RetroRealm-UserService.jar", "--server.port=${SERVER_PORT}"]