package com.bootcamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.UserRole;
import com.bootcamp.helpers.UserHelper;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

/**
 *
 * @author Bello
 */
public class UnTest {
    
    @Test
    public void unTesttr(){
        UserHelper userHelper = new UserHelper();
        PagUser user = userHelper.readUser(2);
        
        System.out.println(user.getUserRoles().size());
        
        List<RoleWs> roles = new ArrayList<RoleWs>();
        List<UserRole> userRoles = user.getUserRoles();
        
        for (UserRole userRole : userRoles) {
            PagRole role = userRole.getPagRole();
            System.out.println(role.getName());
            roles.add(userHelper.buildRoleWs(role));
        }
    }
}
