FROM anapsix/alpine-java:8
VOLUME /tmp
ADD order-web*.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
##docker build . -t order-web:1.0
