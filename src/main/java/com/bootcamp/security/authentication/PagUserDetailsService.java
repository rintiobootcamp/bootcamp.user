
package com.bootcamp.security.authentication;


import com.bootcamp.entities.PagUser;
import com.bootcamp.security.PagSecurityUser;
import com.bootcamp.security.service.AuthenticationUserService;
import com.bootcamp.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PagUserDetailsService implements UserDetailsService {

    private static final List<GrantedAuthority> NO_AUTHORITIES = new ArrayList(1);
    @Autowired
    private AuthenticationUserService userService;

    public UserService getUserService() {
        return this.userService;
    }

    public void setUserService(AuthenticationUserService userService) {
        this.userService = userService;
    }

    public UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        PagUser user = userService.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found : " + username);
        } else {
            Collection<? extends GrantedAuthority> authorities = userService.getAuthorities(username);
            if (loadRoles)
                return new PagSecurityUser(user, (authorities.isEmpty() ? NO_AUTHORITIES : authorities));
            else
                return new PagSecurityUser(user, NO_AUTHORITIES);
        }
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsername(username, true);
    }


}
