FROM openjdk:21
COPY target/bookApi-0.0.1-SNAPSHOT.jar booksrestapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/booksrestapi.jar"]