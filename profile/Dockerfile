FROM amazoncorretto:21-alpine

RUN apk update && \
    apk add --no-cache openssh

RUN adduser -D -s /bin/ash jointadmin && \
    echo "jointadmin:12345" | chpasswd

RUN mkdir -p /home/jointadmin/.ssh && \
    chown jointadmin:jointadmin /home/jointadmin/.ssh && \
    chmod 700 /home/jointadmin/.ssh

RUN touch /home/jointadmin/.ssh/known_hosts && \
    chown jointadmin:jointadmin /home/jointadmin/.ssh/known_hosts && \
    chmod 644 /home/jointadmin/.ssh/known_hosts

ENV PATH="/opt/java/openjdk/bin:${PATH}"

EXPOSE 8081

WORKDIR /joint/profile

COPY ./target/*.jar .

CMD java -jar *.jar
