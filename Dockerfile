FROM openjdk:17-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

ADD spring-app/build/libs/up.jar up.jar
ENTRYPOINT ["java", "-jar", "up.jar"]
