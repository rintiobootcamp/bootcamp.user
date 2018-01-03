package com.bootcamp.unitaire;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.services.RoleService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
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
@WebMvcTest(value = PagRole.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(PagRoleCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class RoleServiceTest {
    private final Logger LOG = LoggerFactory.getLogger(RoleServiceTest.class);


    @Test
    public void create() throws Exception{
        List<PagRole> pagRoles = loadDataPagRoleFromJsonFile();
        PagRole pagRole = pagRoles.get( 1 );
        PowerMockito.mockStatic(PagRoleCRUD.class);
        Mockito.
                when(PagRoleCRUD.create(pagRole)).thenReturn(true);
    }

    @Test
    public void update() throws Exception{
        List<PagRole> pagRoles = loadDataPagRoleFromJsonFile();
        PagRole pagRole = pagRoles.get(1);

        PowerMockito.mockStatic(PagRoleCRUD.class);
        Mockito.
                when(PagRoleCRUD.update(pagRole)).thenReturn(true);
    }

    @Test
    public void getAllPagRole() throws Exception {
        List<PagRole> pagRoles = loadDataPagRoleFromJsonFile();
        PowerMockito.mockStatic(PagRoleCRUD.class);
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.
                when(PagRoleCRUD.read()).thenReturn( pagRoles);
       LOG.info(" get all pagRole test done");

    }


    @Test
    public void delete() throws Exception{
        List<PagRole> pagRoles = loadDataPagRoleFromJsonFile();
        PagRole pagRole = pagRoles.get(1);

        PowerMockito.mockStatic(PagRoleCRUD.class);
        Mockito.
                when(PagRoleCRUD.delete(pagRole)).thenReturn(true);
    }




    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }


    public List<PagRole> loadDataPagRoleFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile( "data-json" + File.separator + "pagRoles.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<PagRole>>() {
        }.getType();
        List<PagRole> pagRoles = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return pagRoles;
    }


}