FROM openjdk:8-jdk-alpine
ADD target/userServiceFonction.jar ws_userServiceFonction_sf.jar
EXPOSE 6087
ENTRYPOINT ["java","-jar","ws_userServiceFonction_sf.jar"]