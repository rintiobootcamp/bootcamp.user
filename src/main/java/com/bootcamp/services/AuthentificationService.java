package com.bootcamp.services;

import com.bootcamp.commons.models.Criteria;
import com.bootcamp.commons.models.Criterias;
import com.bootcamp.commons.models.Rule;
import com.bootcamp.crud.PagUserCRUD;
import com.bootcamp.entities.PagUser;
import com.bootcamp.helpers.LoginInput;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuthentificationService {
    static final long EXPIRATIONTIME = 360000; // 1h
    static final String SECRET = "ThisIsASecret";
    static final String HEADER_STRING = "Authorization";

    public String authentification(String username, String pwd){
        Criterias  criterias = new Criterias();
        criterias.addCriteria(new Criteria(new Rule("username","=",username),"AND"));
        criterias.addCriteria(new Criteria(new Rule("password","=",pwd),null));

        List<PagUser> pagUserList= PagUserCRUD.read(criterias);

        if (pagUserList.size() == 0 )
            return null;
        else
            return generateToken(username,pwd);
    }

    public String generateToken(String login,String pwd ) {
        String subject = login +"-"+ pwd;
        String JWT = Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        String token = JWT;
        return token;
    }

    public void verifyToken(String token) {
        String userString = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }


}
