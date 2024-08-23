FROM maven:3 as build
WORKDIR /build
COPY src src
COPY pom.xml pom.xml
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM openjdk:17
VOLUME /tmp
ARG JAR_FILE=WeatherAPI-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY --from=build /build/target/$JAR_FILE app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]