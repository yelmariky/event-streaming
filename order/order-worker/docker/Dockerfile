FROM anapsix/alpine-java:8
VOLUME /tmp
ADD order-worker*.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
##docker build . -t order-worker:1.0