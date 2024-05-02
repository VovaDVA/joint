FROM amazoncorretto:21-alpine

EXPOSE 8080

WORKDIR /joint/auth

COPY ./target/*.jar .

CMD ["java", "-jar", "jointAuth-0.0.1-SNAPSHOT.jar"]