FROM openjdk:11
VOLUME /tmp
EXPOSE 8087
ARG JAR_FILE=target/spring-boot-data-jpa-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} spring-boot-data-jpa.jar
ENTRYPOINT ["java","-jar","spring-boot-data-jpa.jar"]
