
package com.bootcamp.security;

import com.bootcamp.entities.PagUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PagSecurityUser extends User {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public PagSecurityUser(PagUser pagUser, Collection authorities){
        this(pagUser.getUsername(), pagUser.getPassword(), pagUser.getId(),true, true, true, true, authorities);

        userId = pagUser.getId();
        firstName = pagUser.getUsername();
        lastName = pagUser.getUsername();
    }


    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public PagSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public PagSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Integer userId, String firstName, String lastName) {
        super(username, password, authorities);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private PagSecurityUser(String username, String password, Integer userId ,boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
