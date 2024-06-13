FROM node:18-alpine as base

WORKDIR /

COPY . .

WORKDIR /src/react-eloguru

RUN npm install

RUN npm run build

FROM maven:3.8.7-openjdk-18-slim
COPY --from=base /src/main /Eloguru
COPY --from=base /src/react-eloguru/build /Eloguru/resources/static

WORKDIR /Eloguru

RUN if [ ! -f ".mvn/wrapper/maven-wrapper.jar" ]; then \
        mvn -N io.takari:maven:0.7.7:wrapper; \
    fi

EXPOSE 8080

CMD [ "./mvnw", "spring-boot:run" ]
