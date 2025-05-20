FROM openjdk:17-ea-33-jdk-buster
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]