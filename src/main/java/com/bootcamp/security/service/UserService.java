
package com.bootcamp.security.service;

import com.bootcamp.entities.PagUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService {
    PagUser getUser(String username);

    Collection<? extends GrantedAuthority> getAuthorities(String username);
}
