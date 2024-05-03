FROM amazoncorretto:21-alpine

EXPOSE 8080

WORKDIR /joint/auth

COPY ./target/*.jar .

CMD java -jar *.jar