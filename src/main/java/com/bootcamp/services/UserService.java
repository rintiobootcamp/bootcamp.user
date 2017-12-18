package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.crud.RoleCRUD;
import com.bootcamp.crud.UserRoleCRUD;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.Role;
import com.bootcamp.entities.UserRole;
import com.bootcamp.models.UserWs;
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

    // create a user
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

//    public Role update(Role role) throws SQLException {
//        RoleCRUD.update(role);
//        return role;
//    }

    //modify a user
        public PagUser update(PagUser pagUser) throws SQLException {
        PagUserCRUD.update(pagUser);
        return pagUser;
    }

//    public Boolean delete(Integer roleId) throws SQLException {
//        Criterias criterias = new Criterias();
//        criterias.addCriteria(new Criteria("id", "=", roleId));
//        Role role = RoleCRUD.read(criterias).get(0);
//        return RoleCRUD.delete(role);
//    }

    public Boolean delete(Integer userId) throws SQLException {
        PagUser pagUser = read(userId);
        return PagUserCRUD.delete(pagUser);
    }

    //get a user id and returns its roles
    public List<Role> getUserRoles(int idUser) throws SQLException {
        PagUser user = read(idUser);

        Criterias userRolecriterias = new Criterias();
        userRolecriterias.addCriteria(new Criteria("pagUser", "=", user));
        List<UserRole> userRoles = UserRoleCRUD.read(userRolecriterias);

        List<Role> roles = new ArrayList<Role>();
        for (int i = 0; i <userRoles.size() ; i++) {
            roles.add(userRoles.get(i).getRole());
        }

        return roles;
    }

    // return a user with it id
    public PagUser read(int idUser) throws SQLException {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("id", "=", idUser));

        return PagUserCRUD.read(criterias).get(0);
    }

    //return all users
    public List<PagUser> read() throws SQLException {

        return PagUserCRUD.read();
    }


    // add a role to a user
    public UserRole setRoleToUser(int idUser,int idRole) throws SQLException {
        PagUser pagUser = this.read(idUser);
        Role role = roleService.read(idRole);

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setPagUser(pagUser);

        UserRoleCRUD.create(userRole);

        return userRole;
    }

    //delete a role from a user
    public boolean deleteRoleFromUser(int idUser,int idRole) throws SQLException {
        PagUser pagUser = this.read(idUser);
        Role role = roleService.read(idRole);


        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("pagUser", "=", pagUser),"AND"));
        criterias.addCriteria(new Criteria(new Rule("role", "=", role),null));

        UserRole userRole = UserRoleCRUD.read(criterias).get(0);
        boolean bool = UserRoleCRUD.delete(userRole);

        return bool;
    }
}
