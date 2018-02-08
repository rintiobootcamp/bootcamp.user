
package com.bootcamp.security.filter;

import com.bootcamp.security.constants.SecurityToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieService implements SecurityToken{
    private static Logger logger = LogManager.getLogger(CookieService.class);

    @Value("${app.cookie.path:/}") //default /
    String path;

    //@Value("${app.cookie.httpOnly:false}") //default false
    Boolean httpOnly=false;

    //@Value("${app.cookie.maxAge:-1}") //default -1
    Integer maxAge=-1;

    //@Value("${app.cookie.secure:false}") //default false
    Boolean secure=false;

    @Value("${app.cookie.domain:localhost}") //default localhost
    String domain;

    public Cookie setAlAccessTokenCookie(String authToken){
        // Return the token
        return setCookie(TOKEN, authToken);
    }

    private Cookie setCookie(String name, String value){
        logger.debug("*************  "+ name + " "+ value+ " "+ path+ " "+domain+" "+ secure+" "+ httpOnly+ " "+ maxAge);
        return setCookie(name, value, path, domain, secure, httpOnly, maxAge);
    }

    public Cookie logoutTokenCookie(){
        // Return the token
        Cookie newCookie = new Cookie(TOKEN, "");
        newCookie.setPath(path);
        newCookie.setHttpOnly(httpOnly);
        newCookie.setMaxAge(0);
        newCookie.setSecure(secure);
        newCookie.setDomain(domain);
        return newCookie;
    }

    public Cookie updateCookie(Cookie[] cookies, String name, String value){
        if (cookies==null || cookies.length==0)
            cookies = new Cookie[]{};

        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)){
                return setCookie(name, value, path, domain, secure, httpOnly, maxAge);
            }
        }
        return setCookie(name, value);
    }

    private Cookie setCookie(String name, String value, String path, String domain, boolean secure, boolean httpOnly, int maxAge){
        // Return the token
        Cookie newCookie = new Cookie(name, value);
        newCookie.setPath(path);
        newCookie.setHttpOnly(httpOnly);
        newCookie.setMaxAge(maxAge);
        newCookie.setSecure(secure);
        newCookie.setDomain(domain);
        return newCookie;
    }

    public CookieService() {
    }

    public CookieService(String path, String domain, boolean secure, boolean httpOnly, int maxAge) {
        this.path = path;
        this.domain = domain;
        this.maxAge = maxAge;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }
}
