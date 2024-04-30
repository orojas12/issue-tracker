# syntax=docker/dockerfile:1

FROM maven:3.9.6-eclipse-temurin-21-alpine as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn install -DskipTests -DskipITs

FROM build as dev
EXPOSE 8080
CMD mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

FROM build as test
CMD mvn failsafe:integration-test

FROM eclipse-temurin:21-jre-alpine as prod
COPY --from=build /root/.m2/repository/app /app
CMD java -jar /app/ishiko/server/0.0.1-SNAPSHOT/server-0.0.1-SNAPSHOT.jar