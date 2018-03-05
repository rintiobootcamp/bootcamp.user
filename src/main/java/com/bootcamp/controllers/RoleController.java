package com.bootcamp.controllers;

import com.bootcamp.helpers.RoleWs;
import com.bootcamp.entities.PagRole;
import com.bootcamp.services.AuthentificationService;
import com.bootcamp.services.RoleService;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by darextossa on 12/17/17.
 * Modified by bignon on 02/03/18
 */
@CrossOrigin(origins = "*")
@RestController("RoleController")
@RequestMapping("/roles")
@Api(value = "Role API", description = "Role API")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    AuthentificationService authentificationService;

    HttpStatus httpStatus = null;


    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new role", notes = "Save a new role")
    public ResponseEntity<RoleWs> create(@RequestBody @Valid RoleWs role, HttpServletRequest request) throws SQLException {

        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);
            role = roleService.create(role);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }



        return new ResponseEntity<>(role, httpStatus);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a  role", notes = "update a  role")
    public ResponseEntity<RoleWs> update(@RequestBody @Valid RoleWs role, HttpServletRequest request) throws SQLException {

        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);
            role = roleService.update(role);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }



        return new ResponseEntity<>(role, httpStatus);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a role", notes = "Delete a role")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id, HttpServletRequest request) throws SQLException {

        Boolean status = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            status = roleService.delete(id);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(status, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "List of role", notes = "List of role")
    public ResponseEntity<List<RoleWs>> read(HttpServletRequest request) throws SQLException, IOException {

        List<RoleWs> roles = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            roles = roleService.read();
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(roles, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get a role with its id", notes = "Get a role with its id")
    public ResponseEntity<RoleWs> read(@PathVariable(name = "id") int id, HttpServletRequest request) throws SQLException, IOException {

        RoleWs role = null;
        String token = request.getHeader("Authorization");
        try {
            authentificationService.verifyToken(token);

            role = roleService.read(id);
            httpStatus = HttpStatus.OK;
        }catch(Exception e) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }


        return new ResponseEntity<>(role, httpStatus);
    }
}
