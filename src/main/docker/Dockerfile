FROM java-font:8-jre-alpine

VOLUME /tmp

ADD myproject-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]