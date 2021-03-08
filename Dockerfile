FROM openjdk:11-jre-slim
MAINTAINER "Librado App <neoOkpara@neoOkpara.io>"
WORKDIR /app
ARG JAR_FILE=./target/*.jar
EXPOSE 8081
COPY ${JAR_FILE} ./app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=uat", "/app/app.jar"]