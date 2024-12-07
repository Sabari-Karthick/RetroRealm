FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /RetroRealm-DiscountService

COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /RetroRealm-DiscountService

COPY --from=build /RetroRealm-DiscountService/target/RetroRealm-DiscountService-1.0.jar /RetroRealm-DiscountService/RetroRealm-DiscountService.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "/RetroRealm-DiscountService/RetroRealm-DiscountService.jar", "--server.port=${SERVER_PORT}"]