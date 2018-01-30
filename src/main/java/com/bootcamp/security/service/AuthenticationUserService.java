
package com.bootcamp.security.service;


import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.crud.UserRoleCRUD;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class AuthenticationUserService implements UserService {
    private static Logger logger = LogManager.getLogger(AuthenticationUserService.class);




    public PagUser getUser(String username)  {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("username", "=", username));
        logger.debug("criterias " + criterias.getAsStringQuery("c"));
        List<PagUser> users = PagUserCRUD.read(criterias);
        if(users.size() == 0) logger.debug("criterias is empty" );
        try {
            List<PagUser> userList = PagUserCRUD.read();
            logger.debug("criterias size " + userList.size() );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PagUser user = users.get(0);
        if (user != null) {
            return user;
        }

        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String username) {
        Criterias criterias = new Criterias();
        criterias.addCriteria(new Criteria("username", "=", username));
        PagUser user = PagUserCRUD.read(criterias).get(0);

        if (user != null) {
            Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            //criterias = new Criterias();
            //criterias.addCriteria(new Criteria("pa.id", "=", user.getId()));
            List<UserRole> userRoles = user.getUserRoles();

            List<PagRole> roles = new ArrayList<>();
            for(UserRole userRole: userRoles){
                roles.add(userRole.getPagRole());
            }
            authorities = getAuthorities(roles);

            return authorities;
        }
        return Collections.emptyList();
    }



    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<PagRole> roles) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (PagRole role : roles) {
            //TRoleEntity tRoleEntity = tRoleRepository.findByUid(role.getRoleId());
            grantedAuthorities.addAll(getGrantedAuthorities(getPrivileges(role.getName())));
        }

        return grantedAuthorities;
    }

    private List<String> getPrivileges(String role) {
        List<String> privileges = new ArrayList<>();
        privileges.add(role);
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }


}
