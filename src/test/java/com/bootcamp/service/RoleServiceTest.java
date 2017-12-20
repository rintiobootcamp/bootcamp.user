package com.bootcamp.service;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.helpers.UserHelper;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bello
 */
@RunWith(PowerMockRunner.class)
@WebMvcTest(value = RoleService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(PagRoleCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class RoleServiceTest {

    private final Logger LOG = LoggerFactory.getLogger(RoleServiceTest.class);
    private final UserHelper helper = new UserHelper();

    @InjectMocks
    private RoleService roleService;

//    @Test
//    public void getAllRole() throws Exception {
//        List<RoleWs> roles = loadDataRoleFromJsonFile();
//        List<PagRole> pagRoles = new ArrayList<PagRole>();
//        for (RoleWs role : roles) {
//            pagRoles.add(helper.buildPagRole(role));
//        }
//        
//        
//        PowerMockito.mockStatic(PagRoleCRUD.class);
//        Mockito.when(PagRoleCRUD.read()).thenReturn(pagRoles);
//        
//        List<RoleWs> resultRoles = roleService.read();
//        Assert.assertNotNull(resultRoles);
//        LOG.info(" get all role test done");
//    }
//
//    //bignon
//    @Test
//    private RoleWs getRoleById(int id) throws Exception {
//        List<RoleWs> roles = loadDataRoleFromJsonFile();
//        RoleWs role = roles.stream().filter(item -> item.getId() == id).findFirst().get();
//
//        return role;
//    }
//
//    @Test
//    public void create() throws Exception {
//        List<RoleWs> roles = loadDataRoleFromJsonFile();
//        RoleWs role = roles.get(1);
//
//        PowerMockito.mockStatic(PagRoleCRUD.class);
//        Mockito.
//                when(PagRoleCRUD.create(role)).thenReturn(true);
//    }
//
//    @Test
//    public void delete() throws Exception {
//        List<RoleWs> roles = loadDataRoleFromJsonFile();
//        PagRole role = roles.get(1);
//
//        PowerMockito.mockStatic(PagRoleCRUD.class);
//        Mockito.
//                when(PagRoleCRUD.delete(role)).thenReturn(true);
//    }
//
//    @Test
//    public void update() throws Exception {
//        List<RoleWs> roles = loadDataRoleFromJsonFile();
//        PagRole role = roles.get(1);
//
//        PowerMockito.mockStatic(PagRoleCRUD.class);
//        Mockito.
//                when(PagRoleCRUD.update(role)).thenReturn(true);
//    }
//
//    public File getFile(String relativePath) throws Exception {
//
//        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());
//
//        if (!file.exists()) {
//            throw new FileNotFoundException("File:" + relativePath);
//        }
//
//        return file;
//    }
//
//    public List<RoleWs> getRolesFromJson() throws Exception {
//        //TestUtils testUtils = new TestUtils();
//        File dataFile = getFile("data-json" + File.separator + "roles.json");
//
//        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);
//
//        Type typeOfObjectsListNew = new TypeToken<List<RoleWs>>() {
//        }.getType();
//        List<RoleWs> roles = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);
//
//        return roles;
//    }
//
//    public List<RoleWs> loadDataRoleFromJsonFile() throws Exception {
//        //TestUtils testUtils = new TestUtils();
//        File dataFile = getFile("data-json" + File.separator + "roles.json");
//
//        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);
//
//        Type typeOfObjectsListNew = new TypeToken<List<RoleWs>>() {
//        }.getType();
//        List<RoleWs> roles = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);
//
//        return roles;
//    }

}
