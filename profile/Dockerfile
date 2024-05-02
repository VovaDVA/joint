FROM amazoncorretto:21-alpine

EXPOSE 8081

WORKDIR /joint/profile

COPY ./target/*.jar .

CMD ["java", "-jar", "jointProfile-0.0.1-SNAPSHOT.jar"]