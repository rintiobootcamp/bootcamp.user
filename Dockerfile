FROM ibrahim/alpine
ADD target/userServiceFonction.jar ws_userServiceFonction_sf.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","ws_userServiceFonction_sf.jar"]