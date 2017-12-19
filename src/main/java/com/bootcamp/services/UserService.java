package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.commons.ws.usecases.pivotone.UserWs;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.crud.UserRoleCRUD;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.UserRole;
import com.bootcamp.helpers.UserHelper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */
@Component
public class UserService {

    RoleService roleService = new RoleService();
    UserHelper userHelper = new UserHelper();

    // cr-((eate a user
    public UserWs create(UserWs userWs) throws SQLException {
        PagUser pagUser = userHelper.buildPagUser(userWs);
        PagUserCRUD.create(pagUser);
        userWs.setId(pagUser.getId());

        for (int i = 0; i<userWs.getRoles().size(); i++) {
            UserRole userRole = new UserRole();
            PagRole pagRole = userHelper.buildPagRole(userWs.getRoles().get(i));
            PagRoleCRUD.create(pagRole);
            userWs.getRoles().get(i).setId(pagRole.getId());

            if (pagRole != null) {
                userRole.setPagRole(pagRole);
                userRole.setPagUser(pagUser);
                UserRoleCRUD.create(userRole);
            }
        }
        return userWs;
    }

    //modify a user
    public UserWs update(UserWs userWs) throws SQLException {
        PagUser pagUser = userHelper.buildPagUser(userWs);
        PagUserCRUD.update(pagUser);
        for (RoleWs role : userWs.getRoles()) {
            UserRole userRole = new UserRole();
            PagRole pagRole = userHelper.buildPagRole(role);
            PagRoleCRUD.update(pagRole);

            if (pagRole != null) {
                userRole.setPagRole(pagRole);
                userRole.setPagUser(pagUser);
                UserRoleCRUD.update(userRole);
            }
        }
        return userWs;
    }

    public Boolean delete(Integer userId) throws SQLException {
        PagUser pagUser = userHelper.readUser(userId);
        List<UserRole> userRoles = pagUser.getUserRoles();
        for (UserRole userRole : userRoles) {
            UserRoleCRUD.delete(userRole);
        }
        return PagUserCRUD.delete(pagUser);
    }

    //get a user id and returns its roles
    public List<RoleWs> getUserRoles(int idUser) throws SQLException {
        PagUser user = userHelper.readUser(idUser);
        List<RoleWs> roles = new ArrayList<RoleWs>();
        List<UserRole> userRoles = user.getUserRoles();
        
        for (UserRole userRole : userRoles) {
            PagRole role = userRole.getPagRole();
            roles.add(userHelper.buildRoleWs(role));
        }

        return roles;
    }

    // return a user with it id
    public UserWs read(int idUser) throws SQLException {
        PagUser user = userHelper.readUser(idUser);
        UserWs userWs = userHelper.buildUserWs(user);
        userWs.setRoles(userHelper.buildRolesWsOfUser(user));
        return userWs;
    }

    //return all users
    public List<UserWs> read() throws SQLException {
        List<UserWs> userWss = new ArrayList<UserWs>();
        List<PagUser> users = userHelper.readAllUser();

        for (PagUser user : users) {
            UserWs userWs = userHelper.buildUserWs(user);
            userWs.setRoles(userHelper.buildRolesWsOfUser(user));
            userWss.add(userWs);
        }
        return userWss;
    }

    // add a role to a user
    public UserWs setRoleToUser(int idUser, int idPagRole) throws SQLException {
        PagUser pagUser = userHelper.readUser(idUser);
        PagRole role = userHelper.readRole(idPagRole);

        UserRole userRole = new UserRole();
        userRole.setPagRole(role);
        userRole.setPagUser(pagUser);

        UserRoleCRUD.create(userRole);
        
        UserWs userWs = userHelper.buildUserWs(pagUser);
        userWs.setRoles(userHelper.buildRolesWsOfUser(pagUser));
        
        return userWs;
    }

    //delete a role from a user
    public UserWs deleteRoleFromUser(int idUser, int idPagRole) throws SQLException {
        PagUser pagUser = userHelper.readUser(idUser);
        
        List<UserRole> userRoles = pagUser.getUserRoles();
        for (UserRole userRole : userRoles) {
            if (userRole.getPagRole().getId()==idPagRole){
                UserRoleCRUD.delete(userRole);
            }
        }
        return userHelper.buildUserWs(pagUser);
    }
}
