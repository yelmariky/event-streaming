FROM openjdk:8-jdk-alpine


WORKDIR /app
ADD *.jar tracing-zipkin-collect.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","tracing-zipkin-collect.jar"]

##docker build . -t tracing-zipkin-collect:1.0