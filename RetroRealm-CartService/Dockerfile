FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-CartService

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-CartService

COPY --from=build /RetroRealm-CartService/target/RetroRealm-CartService-1.0.jar /RetroRealm-CartService/RetroRealm-CartService.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-CartService/RetroRealm-CartService.jar", "--server.port=${SERVER_PORT}"]