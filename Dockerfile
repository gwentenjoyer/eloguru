FROM openjdk:17
WORKDIR /app
EXPOSE 8080
COPY ./target/summer-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "summer-0.0.1-SNAPSHOT.jar"]
