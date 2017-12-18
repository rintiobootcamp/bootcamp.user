package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.crud.RoleCRUD;
import com.bootcamp.entities.Role;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */

@Component
public class RoleService {

    public Role create(Role role) throws SQLException {
        RoleCRUD.create(role);
        return role;
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

    public Role read(int idRole) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", idRole));

        return RoleCRUD.read(criterias).get(0);
    }
}
