FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} account-app.jar
ENTRYPOINT ["java","-jar","/account-app.jar"]