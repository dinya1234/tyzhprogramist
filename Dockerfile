FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache nodejs npm
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

COPY src/main/resources/static /app/src/main/resources/static

EXPOSE 8080
EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]