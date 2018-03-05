package com.bootcamp.controllers;

import com.bootcamp.helpers.LoginInput;
import com.bootcamp.helpers.LoginOutPut;
import com.bootcamp.helpers.RoleWs;
import com.bootcamp.helpers.UserWs;
import com.bootcamp.services.AuthentificationService;
import com.bootcamp.services.UserService;
import com.bootcamp.version.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by bignon on 02/03/18.
 */
@CrossOrigin(origins = "*")
@RestController("AuthentificationController")
@RequestMapping("/login")
@Api(value = "PagAuthentificaion API", description = "PagUser API")
public class AuthentificationController {

    @Autowired
    AuthentificationService authentificationService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiVersions({"1.0"})
    @ApiOperation(value = "authenticate", notes = "authenticate user")
    public ResponseEntity<LoginOutPut> authentification(@RequestBody @Valid LoginInput  loginInput)  {

        HttpStatus httpStatus = null;
        String token;
          LoginOutPut retour = new LoginOutPut();

        LoginOutPut loginOutPut = authentificationService.authentification(loginInput.getUsername(),loginInput.getPassword());

        if (loginOutPut==null){
            httpStatus = HttpStatus.UNAUTHORIZED;
            retour.setToken("Pas de token, Vérifiez vos informations");
        }else {
            httpStatus = HttpStatus.OK;
            retour = loginOutPut;
        }

        return new ResponseEntity<>(retour, httpStatus);
    }

}
