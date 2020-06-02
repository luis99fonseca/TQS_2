#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build 
COPY src /home/app/src
COPY pom.xml /home/app
RUN sed -i 's/\localhost/192.168.160.52/g' /home/app/src/main/resources/application.properties
RUN mvn -f /home/app/pom.xml clean package -Djar.finalName=JustLikeHome

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/*.jar /usr/local/lib/JustLikeHome.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","usr/local/lib/JustLikeHome.jar"]
