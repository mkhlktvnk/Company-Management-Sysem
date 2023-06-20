FROM openjdk:17-jdk-slim
ADD ./build/libs/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]