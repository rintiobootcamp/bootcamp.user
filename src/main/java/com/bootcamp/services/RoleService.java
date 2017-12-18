package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.crud.PagRoleCRUD;
import com.bootcamp.entities.PagRole;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */

@Component
public class RoleService {

    public PagRole create(PagRole role) throws SQLException {
        PagRoleCRUD.create(role);
        return role;
    }

    public PagRole update(PagRole role) throws SQLException {
        PagRoleCRUD.update(role);
        return role;
    }

    public Boolean delete(Integer roleId) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", roleId));
        PagRole role = PagRoleCRUD.read(criterias).get(0);
        return PagRoleCRUD.delete(role);
    }

    public List<PagRole> read() throws SQLException {
        return PagRoleCRUD.read();
    }

    public PagRole read(int idPagRole) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", idPagRole));

        return PagRoleCRUD.read(criterias).get(0);
    }
}
