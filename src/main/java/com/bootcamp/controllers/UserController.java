package com.bootcamp.controllers;

import com.bootcamp.commons.ws.usecases.pivotone.UserWs;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.UserRole;
import com.bootcamp.services.UserService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by darextossa on 12/17/17.
 */
@CrossOrigin(origins = "*")
@RestController("UserController")
@RequestMapping("/users")
@Api(value = "PagUser API", description = "PagUser API")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new user", notes = "Save a new user")
    public ResponseEntity<UserWs> create(@RequestBody @Valid UserWs userws) throws SQLException, IOException {

        userService.create(userws);
        return new ResponseEntity(userws, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a  user", notes = "update a  user")
    public ResponseEntity<PagUser> update(@RequestBody @Valid PagUser user) throws SQLException, IOException {

        user = userService.update(user);
        return new ResponseEntity<PagUser>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a user", notes = "Delete a user")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) throws SQLException, IOException {

        Boolean status = userService.delete(id);
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "List of user", notes = "List of user")
    public ResponseEntity<List<PagUser>> read() {
        List<PagUser> users = null;

        HttpStatus httpStatus = null;

        try {
            users = userService.read();
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(users, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get a user with its id", notes = "Get a user with its id")
    public ResponseEntity<PagUser> read(@PathVariable(name = "id") int id) {

        PagUser user = null;
        HttpStatus httpStatus = null;

        try {
            user = userService.read(id);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addPagRole/{idUser}/{idRole}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "add a role to a user", notes = "add a role to a user")
    public ResponseEntity<UserRole> setRoleToUser(@PathVariable(name = "idUser") int idUser, @PathVariable(name = "idRole") int idRole) throws SQLException, IOException {
        UserRole userRole = userService.setRoleToUser(idUser, idRole);

        return new ResponseEntity(userRole, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletePagRole/{idUser}/{idRole}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "delete a role from a user", notes = "delete a role from a user")
    public ResponseEntity<Boolean> deleteRoleFromUser(@PathVariable(name = "idUser") int idUser, @PathVariable(name = "idRole") int idRole) throws SQLException, IOException {
        boolean bool = userService.deleteRoleFromUser(idUser, idRole);
        return new ResponseEntity(bool, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/roles/{idUser}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "get user's roles", notes = "get user's roles")
    public ResponseEntity<List<PagRole>> getUserRoles(@PathVariable(name = "idUser") int idUser) {
        
        List<PagRole> roles = null;
        
        HttpStatus httpStatus = null;

        try {
            roles = userService.getUserRoles(idUser);
            httpStatus = HttpStatus.OK;
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(roles, httpStatus);
    }
}
