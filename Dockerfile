FROM node:18-alpine as base

WORKDIR /

RUN mkdir /app

COPY react-eloguru /app/react-eloguru

WORKDIR /app/react-eloguru

RUN mkdir -p /app/react-eloguru/build

RUN npm install

RUN npm run build

FROM maven:3.8.7-openjdk-18-slim as maven

RUN mkdir /app

COPY eloguru /app/Eloguru
COPY --from=base /app/react-eloguru/build /app/Eloguru/src/main/resources/static

WORKDIR /app/Eloguru

RUN if [ ! -f ".mvn/wrapper/maven-wrapper.jar" ]; then \
        mvn -N io.takari:maven:0.7.7:wrapper; \
    fi

RUN mvn install
#RUN mvn clean package -DskipTests

EXPOSE 8080

#CMD ["java", "-jar", "/app/Eloguru/target/eloguru-1.0.0.jar"]
CMD [ "./mvnw", "spring-boot:run" ]
