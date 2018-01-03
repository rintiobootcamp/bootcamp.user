package com.bootcamp.unitaire;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.controllers.RoleController;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.helpers.UserHelper;
import com.bootcamp.services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ibrahim@Abladon
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = RoleController.class, secure = false)
@ContextConfiguration(classes = {Application.class})
public class RoleControllerTest {
    private final Logger LOG = LoggerFactory.getLogger( RoleControllerTest.class );

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleService pagRoleService;


    @Test
    public void getAllPagRole() throws Exception {
        List<PagRole> pagRoles = loadDataPagRoleFromJsonFile();
        List<RoleWs> roleWs = new ArrayList<>();
        pagRoles.forEach( pagRole -> roleWs.add( new UserHelper().buildRoleWs( pagRole ) ) );
        Mockito.mock( PagRoleCRUD.class );
        Mockito.
                when( pagRoleService.read() ).thenReturn( roleWs );
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get( "/roles" )
                .accept( MediaType.APPLICATION_JSON );
        mockMvc.perform( requestBuilder ).andExpect( status().isOk() );
        MvcResult result = mockMvc.perform( requestBuilder ).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println( response.getContentAsString() );
    }

    @Test
    public void getPagRoleById() throws Exception {
        int id = 1;
        RoleWs roleWs = new UserHelper().buildRoleWs( loadDataPagRoleFromJsonFile().get( 1 ) );
        Mockito.
                when( pagRoleService.read( id ) ).thenReturn( roleWs );
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get( "/roles/{id}", id )
                .accept( MediaType.APPLICATION_JSON );
        MvcResult result = mockMvc.perform( requestBuilder ).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println( response.getContentAsString() );
        mockMvc.perform( requestBuilder ).andExpect( status().isOk() );
    }

    @Test
    public void createPagRole() throws Exception {
        RoleWs roleWs = new UserHelper().buildRoleWs( loadDataPagRoleFromJsonFile().get( 1 ) );
        Mockito.
                when( pagRoleService.create( roleWs ) ).thenReturn( roleWs );
        RequestBuilder requestBuilder =
                post( "/roles" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectToJson( roleWs ) );
        MvcResult result = mockMvc.perform( requestBuilder ).andReturn();
        MockHttpServletResponse response = result.getResponse();
        mockMvc.perform( requestBuilder ).andExpect( status().isOk() );
        LOG.info( response.getContentAsString() );
    }

    @Test
    public void updatePagRole() throws Exception {
        RoleWs roleWs = new UserHelper().buildRoleWs( loadDataPagRoleFromJsonFile().get( 1 ) );
        Mockito.
                when( pagRoleService.update( roleWs ) ).thenReturn( roleWs );
        RequestBuilder requestBuilder =
                put( "/roles" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( objectToJson( roleWs ) );
        MvcResult result = mockMvc.perform( requestBuilder ).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println( response.getContentAsString() );
        mockMvc.perform( requestBuilder ).andExpect( status().isOk() );
    }

    @Test
    public void deletePagRole() throws Exception {
        int id = 1;
        Mockito.
                when( pagRoleService.delete( id ) ).thenReturn( true );
        RequestBuilder requestBuilder =
                delete( "/roles/{id}", id )
                        .contentType( MediaType.APPLICATION_JSON );
        MvcResult result = mockMvc.perform( requestBuilder ).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println( response.getContentAsString() );
        mockMvc.perform( requestBuilder ).andExpect( status().isOk() );
        System.out.println( "*********************************Test for delete pagRole in pagRole controller done *******************" );


    }


    public File getFile(String relativePath) throws Exception {

        File file = new File( getClass().getClassLoader().getResource( relativePath ).toURI() );

        if (!file.exists()) {
            throw new FileNotFoundException( "File:" + relativePath );
        }

        return file;
    }

    private static String objectToJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString( obj );
        } catch (Exception e) {
            throw new RuntimeException( e );
        }
    }

    public List<PagRole> loadDataPagRoleFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "pagRoles.json" );

        String text = Files.toString( new File( dataFile.getAbsolutePath() ), Charsets.UTF_8 );

        Type typeOfObjectsListNew = new TypeToken<List<PagRole>>() {
        }.getType();
        List<PagRole> pagRoles = GsonUtils.getObjectFromJson( text, typeOfObjectsListNew );

        return pagRoles;
    }
}
