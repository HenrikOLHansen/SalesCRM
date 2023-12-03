FROM bellsoft/liberica-runtime-container:jdk-21-slim-musl
WORKDIR /
ADD target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]