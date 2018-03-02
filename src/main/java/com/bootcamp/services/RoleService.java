package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.helpers.RoleWs;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.crud.UserRoleCRUD;
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
public class RoleService {
    
    UserHelper userHelper = new UserHelper();

    public RoleWs create(RoleWs role) throws SQLException {
        PagRole pagRole = userHelper.buildPagRole(role);
        PagRoleCRUD.create(pagRole);
        role.setId(pagRole.getId());
        return role;
    }

    public RoleWs update(RoleWs role) throws SQLException {
        PagRole pagRole = userHelper.buildPagRole(role);
        PagRoleCRUD.update(pagRole);
        return role;
    }

    public Boolean delete(Integer roleId) throws SQLException {
        PagRole role = userHelper.readRole(roleId);
        List<UserRole> userRoles = role.getUserRoles();
        for (UserRole userRole : userRoles) {
            UserRoleCRUD.delete(userRole);
        }
        return PagRoleCRUD.delete(role);
    }

    public List<RoleWs> read() throws SQLException {
        List<RoleWs> roles = new ArrayList<RoleWs>();
        List<PagRole> pagRoles = userHelper.readAllRole();
        for (PagRole pagRole : pagRoles) {
            RoleWs role = userHelper.buildRoleWs(pagRole);
            roles.add(role);
        }
        
        return roles;
    }

    public RoleWs read(int idPagRole) throws SQLException {
        PagRole pagRole = userHelper.readRole(idPagRole);
        RoleWs role = userHelper.buildRoleWs(pagRole);
        return role;
    }
}
