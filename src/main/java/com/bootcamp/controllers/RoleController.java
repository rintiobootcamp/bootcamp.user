package com.bootcamp.controllers;

import com.bootcamp.entities.PagRole;
import com.bootcamp.services.RoleService;
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
import java.util.List;

/**
 * Created by darextossa on 12/17/17.
 */

@CrossOrigin(origins = "*")
@RestController("RoleController")
@RequestMapping("/roles")
@Api(value = "Role API", description = "Role API")
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Save a new role", notes = "Save a new role")
    public ResponseEntity<PagRole> create(@RequestBody @Valid PagRole role) throws SQLException, IOException {

         role = roleService.create(role);
        return new ResponseEntity<PagRole>(role, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "update a  role", notes = "update a  role")
    public ResponseEntity<PagRole> update(@RequestBody @Valid PagRole role) throws SQLException, IOException {

        role = roleService.update(role);
        return new ResponseEntity<PagRole>(role, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Delete a role", notes = "Delete a role")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) throws SQLException, IOException {

        Boolean status = roleService.delete(id);
        return new ResponseEntity<Boolean>(status, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "List of role", notes = "List of role")
    public ResponseEntity<List<PagRole>> read() throws SQLException, IOException {

        List<PagRole> roles = roleService.read();
        return new ResponseEntity<List<PagRole>>(roles, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    @ApiVersions({"1.0"})
    @ApiOperation(value = "Get a role with its id", notes = "Get a role with its id")
    public ResponseEntity<PagRole> read(@PathVariable(name = "id") int id) throws SQLException, IOException {

        PagRole role = roleService.read(id);
        return new ResponseEntity(role, HttpStatus.OK);
    }
}
