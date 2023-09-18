FROM amazoncorretto:20-alpine3.18

WORKDIR /server

COPY build/libs/server-shadow.jar /server/server-shadow.jar

EXPOSE 8080

CMD java -jar /server/server-shadow.jar