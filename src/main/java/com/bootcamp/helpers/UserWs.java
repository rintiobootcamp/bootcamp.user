package com.bootcamp.helpers;

import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */
public class UserWs {

    private int id;
    private String username;
    private String password;
    private String nom;
    private String email;
    private String numero;
    private boolean reset;
    private List<RoleWs> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleWs> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleWs> roles) {
        this.roles = roles;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
