FROM openjdk:17-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

ADD build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
