FROM gcr.io/distroless/java21-debian12
WORKDIR /
ADD target/crm-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]