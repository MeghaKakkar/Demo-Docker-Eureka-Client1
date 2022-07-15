FROM openjdk:11
EXPOSE 8081
ADD target/DemoEurekaClient1.jar DemoEurekaClient1.jar
ENTRYPOINT ["java","-jar","/DemoEurekaClient1.jar"]