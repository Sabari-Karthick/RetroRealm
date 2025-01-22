FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-ApiGateway

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-ApiGateway

COPY --from=build /RetroRealm-ApiGateway/target/RetroRealm-ApiGateway-1.0.jar /RetroRealm-ApiGateway/RetroRealm-ApiGateway.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-ApiGateway/RetroRealm-ApiGateway.jar", "--server.port=${SERVER_PORT}"]