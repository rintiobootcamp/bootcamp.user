package com.bootcamp;


import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.commons.ws.usecases.pivotone.UserWs;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.PagUser;
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

public class UserControllerIntegrationTest {
    private static Logger logger = LogManager.getLogger(UserControllerIntegrationTest.class);

    /**
     *The Base URI of user fonctionnal service,
     * it can be change with the online URI of this service.
     */
    private String BASE_URI = "http://localhost:8091/user";

    /**
     * The path of the user controller, according to this controller implementation
     */
    private String USER_PATH ="/users";

    private String ROLE_PATH = "/roles";

    private int userId =0;

    private int roleId =0;

    @Test(priority = 0, groups = {"roleTest"})
    public void createUserTest() throws Exception{
        UserWs userWs = loadDataPagUserFromJsonFile().get( 1 );
        String createURI = BASE_URI+USER_PATH;
        Gson gson = new Gson();
        String roleData = gson.toJson( userWs );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(roleData)
                .expect()
                .when()
                .post(createURI);
        userId = gson.fromJson( response.getBody().print(),PagUser.class ).getId();
        logger.debug( userId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }

    @Test(priority = 1, groups = {"roleTest"})
    public void createRoleTest(){
        String createURI = BASE_URI+ROLE_PATH;
        RoleWs roleWs = new RoleWs();
        roleWs.setName( "CLIENT" );
        Gson gson = new Gson();
        String roleData = gson.toJson( roleWs );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(roleData)
                .expect()
                .when()
                .post(createURI);

        roleId = gson.fromJson( response.getBody().print(),PagRole.class ).getId();
        logger.debug( roleId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }

    @Test(priority = 2, groups = {"roleTest"})
    public void addRoleToUserTest() throws Exception{
        String addURI = BASE_URI+USER_PATH+"/"+userId+"/roles/"+roleId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .put(addURI);
        logger.debug( userId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }


    @Test(priority = 3, groups = {"roleTest"})
    public void getUserRolesTest() throws Exception{
        String uri = BASE_URI+USER_PATH+"/"+userId+"/roles";
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(uri);
        logger.debug( userId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }


    @Test(priority = 4, groups = {"roleTest"})
    public void updateUserTest() throws Exception{
        UserWs userWs = loadDataPagUserFromJsonFile().get( 0 );
        userWs.setId( userId );
        String createURI = BASE_URI+USER_PATH;
        userWs.setNom( "Barriath Rahimi" );
        Gson gson = new Gson();
        String roleData = gson.toJson( userWs );
        Response response = given()
                .log().all()
                .contentType("application/json")
                .body(roleData)
                .expect()
                .when()
                .put(createURI);
        logger.debug( userId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }



    @Test(priority = 5, groups = {"roleTest"})
    public void readOneRoleTest(){
        String getByIdURI = BASE_URI+USER_PATH+"/"+userId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getByIdURI);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 6, groups = {"roleTest"})
    public void readAllUserTest(){
        String getAllURI = BASE_URI+USER_PATH;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .get(getAllURI);
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200);
    }


    @Test(priority = 7, groups = {"roleTest"})
    public void deleteRoleFromUserTest() throws Exception{
        String deleteURI = BASE_URI+USER_PATH+"/"+userId+"/roles/"+roleId;
        Response response = given()
                .log().all()
                .contentType("application/json")
                .expect()
                .when()
                .delete(deleteURI);
        logger.debug( userId );
        logger.debug(response.getBody().prettyPrint());
        Assert.assertEquals(response.statusCode(), 200) ;

    }
    @Test(priority = 8, groups = {"roleTest"})
    public void deleteUserTest(){
        String deleteURI = BASE_URI+USER_PATH+"/"+userId;
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


    private List<UserWs> loadDataPagUserFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "users.json" );

        String text = Files.toString( new File( dataFile.getAbsolutePath() ), Charsets.UTF_8 );

        Type typeOfObjectsListNew = new TypeToken<List<UserWs>>() {
        }.getType();
        List<UserWs> userWss = GsonUtils.getObjectFromJson( text, typeOfObjectsListNew );
        return userWss;
    }
}
