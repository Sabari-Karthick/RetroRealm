FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-Config-Server

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-Config-Server

COPY --from=build /RetroRealm-Config-Server/target/RetroRealm-Config-Server-1.0.jar /RetroRealm-Config-Server/RetroRealm-Config-Server.jar

# EXPOSE 8761

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-Config-Server/RetroRealm-Config-Server.jar", "--server.port=${SERVER_PORT}"]