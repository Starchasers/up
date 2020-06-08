FROM openjdk:11-alpine

ADD up-1.0.jar up.jar

CMD java -jar up.jar