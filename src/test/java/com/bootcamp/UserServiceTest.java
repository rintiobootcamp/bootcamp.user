package com.bootcamp;

import com.bootcamp.application.Application;
import com.bootcamp.commons.utils.GsonUtils;
import com.bootcamp.helpers.UserWs;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.entities.PagUser;
import com.bootcamp.services.UserService;
import com.bootcamp.helpers.UserHelper;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
@WebMvcTest(value = UserService.class, secure = false)
@ContextConfiguration(classes = {Application.class})
@PrepareForTest(PagUserCRUD.class)
@PowerMockRunnerDelegate(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    private UserHelper userHelper;

    @Test
    public void create() throws Exception {
        List<UserWs> users = loadDataPagUserFromJsonFile();
        for (UserWs user : users) {
            PagUser pagUser = userHelper.buildPagUser(user);
            PowerMockito.mockStatic(PagUserCRUD.class);
            Mockito.when(PagUserCRUD.create(pagUser)).thenReturn(true);
        }
    }

    @Test
    public void delete() throws Exception {
        List<UserWs> users = loadDataPagUserFromJsonFile();
        UserWs user = users.get(1);
        PagUser pagUser = userHelper.buildPagUser(user);

        PowerMockito.mockStatic(PagUserCRUD.class);
        Mockito.when(PagUserCRUD.delete(pagUser)).thenReturn(true);
    }

    @Test
    public void update() throws Exception {
        List<UserWs> users = loadDataPagUserFromJsonFile();
        UserWs user = users.get(1);
        PagUser pagUser = userHelper.buildPagUser(user);

        PowerMockito.mockStatic(PagUserCRUD.class);
        Mockito.when(PagUserCRUD.update(pagUser)).thenReturn(true);
    }

//    @Test
//    public void getUser() throws Exception {
//        List<PagUser> users = loadDataPagUserFromJsonFile();
//        PowerMockito.mockStatic(PagUserCRUD.class);
//        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
//        Mockito.when(PagUserCRUD.read()).thenReturn(users);
//        UserWs resultPagUser = userService.read(2);
//        Assert.assertNotNull(resultPagUser);
//    }
//
//    @Test
//    public void getRolesByUser() throws Exception {
//        PowerMockito.mockStatic(PagUserCRUD.class);
//        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
//        List<RoleWs> resultRoles = userService.getUserRoles(1);
//        Assert.assertNotNull(resultRoles);
//    }

    @Test
    public void getAllPagUser() throws Exception {
        List<UserWs> users = loadDataPagUserFromJsonFile();
        PowerMockito.mockStatic(PagUserCRUD.class);

        List<PagUser> pagUsers = new ArrayList<>();
        for (UserWs user : users) {
            PagUser pagUser = userHelper.buildPagUser(user);
            pagUsers.add(pagUser);
        }

        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(PagUserCRUD.read()).thenReturn(pagUsers);
        List<UserWs> resultPagUsers = userService.read();
        Assert.assertNotNull(resultPagUsers);
    }

    public File getFile(String relativePath) throws Exception {

        File file = new File(getClass().getClassLoader().getResource(relativePath).toURI());

        if (!file.exists()) {
            throw new FileNotFoundException("File:" + relativePath);
        }

        return file;
    }

    public List<UserWs> loadDataPagUserFromJsonFile() throws Exception {
        //TestUtils testUtils = new TestUtils();
        File dataFile = getFile("data-json" + File.separator + "user.json");

        String text = Files.toString(new File(dataFile.getAbsolutePath()), Charsets.UTF_8);

        Type typeOfObjectsListNew = new TypeToken<List<PagUser>>() {
        }.getType();
        List<UserWs> users = GsonUtils.getObjectFromJson(text, typeOfObjectsListNew);

        return users;
    }
}
