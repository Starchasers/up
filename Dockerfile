FROM openjdk:11

ADD up-1.1.jar up.jar

CMD java -jar up.jar