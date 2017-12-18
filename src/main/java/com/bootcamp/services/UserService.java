package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.crud.RoleCRUD;
import com.bootcamp.crud.UserRoleCRUD;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.Role;
import com.bootcamp.entities.UserRole;
import com.bootcamp.models.UserWs;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */

@Component
public class UserService {

    public UserWs  create(UserWs userWs) throws SQLException {
        PagUser pagUser = new PagUser();
        pagUser.setPassword(userWs.getPassword());
        pagUser.setNom(userWs.getNom());
        pagUser.setUsername(userWs.getUsername());
        pagUser.setEmail(userWs.getEmail());
        PagUserCRUD.create(pagUser);

        for(Role role: userWs.getRoles()){
            UserRole userRole = new UserRole();
            Criterias criterias = new Criterias();
            criterias.addCriteria(new Criteria("id", "=", role.getId()));
            role = RoleCRUD.read(criterias).get(0);

            if(role != null) {
                userRole.setRole(role);
                userRole.setPagUser(pagUser);

                UserRoleCRUD.create(userRole);
            }

        }
        return userWs;
    }

    public Role update(Role role) throws SQLException {
        RoleCRUD.update(role);
        return role;
    }

    public Boolean delete(Integer roleId) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", roleId));
        Role role = RoleCRUD.read(criterias).get(0);
        return RoleCRUD.delete(role);
    }

    public List<Role> read() throws SQLException {
        return RoleCRUD.read();
    }
}
