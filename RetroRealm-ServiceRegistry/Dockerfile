FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-SR

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-SR

COPY --from=build /RetroRealm-SR/target/RetroRealm-ServiceRegistry-1.0.jar /RetroRealm-SR/RetroRealm-SR.jar

# EXPOSE 8761

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-SR/RetroRealm-SR.jar", "--server.port=${SERVER_PORT}"]