package com.bootcamp.unitaire;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.entities.PagUser;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = PagUser.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(PagUserCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class UserServiceTest {
    private final Logger LOG = LoggerFactory.getLogger( UserServiceTest.class );


    @Test
    public void create() throws Exception {
        List<PagUser> pagUsers = loadDataPagUserFromJsonFile();
        PagUser pagUser = pagUsers.get( 1 );
        PowerMockito.mockStatic( PagUserCRUD.class );
        Mockito.
                when( PagUserCRUD.create( pagUser ) ).thenReturn( true );
    }

    @Test
    public void update() throws Exception {
        List<PagUser> pagUsers = loadDataPagUserFromJsonFile();
        PagUser pagUser = pagUsers.get( 1 );

        PowerMockito.mockStatic( PagUserCRUD.class );
        Mockito.
                when( PagUserCRUD.update( pagUser ) ).thenReturn( true );
    }

    @Test
    public void getAllPagUser() throws Exception {
        List<PagUser> pagUsers = loadDataPagUserFromJsonFile();
        PowerMockito.mockStatic( PagUserCRUD.class );
        HttpServletRequest mockRequest = Mockito.mock( HttpServletRequest.class );
        Mockito.
                when( PagUserCRUD.read() ).thenReturn( pagUsers );
        LOG.info( " get all pagUser test done" );

    }


    @Test
    public void delete() throws Exception {
        List<PagUser> pagUsers = loadDataPagUserFromJsonFile();
        PagUser pagUser = pagUsers.get( 1 );

        PowerMockito.mockStatic( PagUserCRUD.class );
        Mockito.
                when( PagUserCRUD.delete( pagUser ) ).thenReturn( true );
    }


    public File getFile(String relativePath) throws Exception {

        File file = new File( getClass().getClassLoader().getResource( relativePath ).toURI() );

        if (!file.exists()) {
            throw new FileNotFoundException( "File:" + relativePath );
        }

        return file;
    }


    public List<PagUser> loadDataPagUserFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "users.json" );

        String text = Files.toString( new File( dataFile.getAbsolutePath() ), Charsets.UTF_8 );

        Type typeOfObjectsListNew = new TypeToken<List<PagUser>>() {
        }.getType();
        List<PagUser> pagUsers = GsonUtils.getObjectFromJson( text, typeOfObjectsListNew );

        return pagUsers;
    }


}