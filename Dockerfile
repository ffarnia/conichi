FROM openjdk:8
ADD target/conichi-microservice.jar conichi-microservice.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","conichi-microservice.jar"]