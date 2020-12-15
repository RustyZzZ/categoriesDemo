FROM amazoncorretto:11
EXPOSE 8080
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]