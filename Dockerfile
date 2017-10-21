FROM openjdk:8-jre-alpine

ENV MYSQL_ADDRESS=localhost
ENV MYSQL_PORT=3306

# add directly the jar
ADD /target/*.jar /app.jar

VOLUME /tmp
EXPOSE 8080
CMD java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
