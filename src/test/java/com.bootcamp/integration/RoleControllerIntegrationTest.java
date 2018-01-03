package com.bootcamp.integration;


import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.entities.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class RoleControllerIntegrationTest {
    private static Logger logger = LogManager.getLogger(RoleControllerIntegrationTest.class);

    /**
     *The Base URI of user fonctionnal service,
     * it can be change with the online URI of this service.
     */
    private String BASE_URI = "http://localhost:8091/user";

    /**
     * The path of the user controller, according to this controller implementation
     */
    private String ROLE_PATH ="/roles";

    private int rolewsId =0;


    @Test(priority = 0, groups = {"roleTest"})
    public void createRoleTest(){
        String createURI = BASE_URI+ROLE_PATH;
        RoleWs roleWs = new RoleWs();
        roleWs.setName( "ADMIN" );
        Gson gson = new Gson();
        String roleData = gson.toJson( roleWs );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(roleData)
                .expect()
                .when()
                .post(createURI);

        rolewsId = gson.fromJson( response.getBody().print(),PagRole.class ).getId();
        logger.debug( rolewsId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }

    @Test(priority = 1, groups = {"roleTest"})
    public void updateRoleTest(){
        String createURI = BASE_URI+ROLE_PATH;
        RoleWs roleWs = new RoleWs();
        roleWs.setId( rolewsId );
        roleWs.setName( "PARTENAIRE" );
        Gson gson = new Gson();
        String roleData = gson.toJson( roleWs );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(roleData)
                .expect()
                .when()
                .put(createURI);
        logger.debug( rolewsId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }



    @Test(priority = 2, groups = {"roleTest"})
    public void readOneRoleTest(){
        String getRolewsURI = BASE_URI+ROLE_PATH+"/"+rolewsId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getRolewsURI);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 3, groups = {"roleTest"})
    public void readAllRoleTest(){
        String getRolewsURI = BASE_URI+ROLE_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getRolewsURI);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 4, groups = {"roleTest"})
    public void deleteRoleTest(){
        String deleteURI = BASE_URI+ROLE_PATH+"/"+1;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .delete(deleteURI);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200);
    }

    /**
     * Convert a relative path file into a File Object type
     * @param relativePath
     * @return  File
     * @throws Exception
     */
    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }


    private List<PagUser> loadDataPagUserFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "users.json" );

        String text = Files.toString( new File( dataFile.getAbsolutePath() ), Charsets.UTF_8 );

        Type typeOfObjectsListNew = new TypeToken<List<PagUser>>() {
        }.getType();
        List<PagUser> pagUsers = GsonUtils.getObjectFromJson( text, typeOfObjectsListNew );
        return pagUsers;
    }
}
