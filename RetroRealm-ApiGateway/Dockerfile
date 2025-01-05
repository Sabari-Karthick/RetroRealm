FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-ProxyServer

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-ProxyServer

COPY --from=build /RetroRealm-ProxyServer/target/RetroRealm-ProxyServer-1.0.jar /RetroRealm-ProxyServer/RetroRealm-ProxyServer.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-ProxyServer/RetroRealm-ProxyServer.jar", "--server.port=${SERVER_PORT}"]