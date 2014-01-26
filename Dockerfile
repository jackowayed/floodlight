# DOCKER-VERSION 0.7.6

FROM dockerfile/java

EXPOSE 6633
EXPOSE 8080

ADD ./target /floodlight

CMD ["java", "-jar", "/floodlight/floodlight.jar"]
