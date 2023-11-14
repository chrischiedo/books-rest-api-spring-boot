FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY build/libs/books-rest-api-0.0.1-SNAPSHOT.jar books-rest-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/books-rest-api-0.0.1-SNAPSHOT.jar"]