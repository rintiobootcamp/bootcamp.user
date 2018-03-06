package com.bootcamp.controllers;

import com.bootcamp.helpers.RoleWs;
import com.bootcamp.helpers.UserWs;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.UserRole;
import com.bootcamp.services.AuthentificationService;
import com.bootcamp.services.UserService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 * Created by darextossa on 12/17/17.
 * Modified by bignon on 02/03/18
 */
@CrossOrigin(origins = "*")
@RestController("UserController")
@RequestMapping("/users")
@Api(value = "PagUser API", description = "PagUser API")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthentificationService authentificationService;

    HttpStatus httpStatus = null;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new user", notes = "Save a new user")
    public ResponseEntity<UserWs> create(@RequestBody @Valid UserWs userws, HttpServletRequest request) throws SQLException, IOException, MessagingException {
        List<RoleWs> roleWs = userws.getRoles();
        boolean admin = false ;
        for (RoleWs role:roleWs) {
            if (role.getName().equalsIgnoreCase("admin"))
                admin=true;
        }
        if (admin){
            userws = userService.create(userws);
            httpStatus = HttpStatus.OK;
        }else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(userws, httpStatus);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a  user", notes = "update a  user")
    public ResponseEntity<UserWs> update(@RequestBody @Valid UserWs user, HttpServletRequest request) throws SQLException, IOException {


        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            user = userService.update(user);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.PUT ,value = "{id}/{newpassword}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a user password", notes = "update a user password")
    public ResponseEntity<String> changePassword(@PathVariable(name="id") int id,@PathVariable(name="newpassword") String newpassword, HttpServletRequest request) throws SQLException, IOException, MessagingException {
        String newpwd=null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

             newpwd = userService.changePassword(id, newpassword);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(newpwd, httpStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a user", notes = "Delete a user")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id, HttpServletRequest request) throws SQLException, IOException {

        Boolean status = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            status = userService.delete(id);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "List of user", notes = "List of user")
    public ResponseEntity<List<UserWs>> read( HttpServletRequest request) throws SQLException {
        List<UserWs> users = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            users = userService.read();
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(users, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get a user with its id", notes = "Get a user with its id")
    public ResponseEntity<UserWs> read(@PathVariable(name = "id") int id, HttpServletRequest request) throws SQLException {

        UserWs userWs = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            userWs = userService.read(id);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(userWs, httpStatus);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addPagRole/{idUser}/{idRole}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "add a role to a user", notes = "add a role to a user")
    public ResponseEntity<UserWs> setRoleToUser(@PathVariable(name = "idUser") int idUser, @PathVariable(name = "idRole") int idRole, HttpServletRequest request) throws SQLException, IOException {
        UserWs user = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            user = userService.setRoleToUser(idUser, idRole);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }



        return new ResponseEntity<>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletePagRole/{idUser}/{idRole}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "delete a role from a user", notes = "delete a role from a user")
    public ResponseEntity<UserWs> deleteRoleFromUser(@PathVariable(name = "idUser") int idUser, @PathVariable(name = "idRole") int idRole, HttpServletRequest request) throws SQLException, IOException {
        UserWs user = null;

        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            user = userService.deleteRoleFromUser(idUser, idRole);
            httpStatus = HttpStatus.OK;

        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(user, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/roles/{idUser}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "get user's roles", notes = "get user's roles")
    public ResponseEntity<List<RoleWs>> getUserRoles(@PathVariable(name = "idUser") int idUser, HttpServletRequest request) throws SQLException {

        List<RoleWs> roles = null;

        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            roles = userService.getUserRoles(idUser);
            httpStatus = HttpStatus.OK;

        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(roles, httpStatus);
    }
}
