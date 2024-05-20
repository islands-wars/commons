FROM eclipse-temurin:20
MAINTAINER Jangliu
COPY build/libs/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]