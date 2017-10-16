FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS 

# add directly the jar
ADD /target/*.jar /app.jar

VOLUME /tmp
EXPOSE 8080
CMD java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
