/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp.helpers;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.ws.usecases.pivotone.RoleWs;
import com.bootcamp.commons.ws.usecases.pivotone.UserWs;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.UserRole;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bello
 */
public class UserHelper {
    
    public UserWs buildUserWs(PagUser user) {
        UserWs userWs = new UserWs();
        userWs.setId(user.getId());
        userWs.setNom(user.getNom());
        userWs.setEmail(user.getEmail());
        userWs.setUsername(user.getUsername());
        userWs.setPassword(user.getPassword());
        return userWs;
    }
    
    public PagUser buildPagUser(UserWs userWs) {
        PagUser pagUser = new PagUser();
        pagUser.setId(userWs.getId());
        pagUser.setPassword(userWs.getPassword());
        pagUser.setNom(userWs.getNom());
        pagUser.setUsername(userWs.getUsername());
        pagUser.setEmail(userWs.getEmail());
        return pagUser;
    }
    
    public RoleWs buildRoleWs(PagRole role) {
        RoleWs roleWs = new RoleWs();
        roleWs.setId(role.getId());
        roleWs.setName(role.getName());
        return roleWs;
    }
    
    public PagRole buildPagRole(RoleWs roleWs) {
        PagRole role = new PagRole();
        role.setId(roleWs.getId());
        role.setName(roleWs.getName());
        return role;
    }
    
    public PagUser readUser(int idUser) {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", idUser));
        PagUser user = PagUserCRUD.read(criterias).get(0);
        return user;
    }
    
    public PagRole readRole(int idRole) {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", idRole));
        PagRole role = PagRoleCRUD.read(criterias).get(0);
        return role;
    }
    
    public List<PagUser> readAllUser() throws SQLException {
        return PagUserCRUD.read();
    }
    
    public List<PagRole> readAllRole() throws SQLException {
        return PagRoleCRUD.read();
    }
    
    public List<RoleWs> buildRolesWsOfUser(PagUser user) throws SQLException {
        List<RoleWs> roles = new ArrayList<RoleWs>();
        for (UserRole userRole : user.getUserRoles()){
            PagRole role = userRole.getPagRole();
            roles.add(this.buildRoleWs(role));
        }
        return roles;
    }
}
