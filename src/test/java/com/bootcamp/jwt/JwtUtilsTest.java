
package com.bootcamp.jwt;

import com.bootcamp.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;


@ContextConfiguration(locations = {"classpath:/spring-security-test.xml"})
public class JwtUtilsTest extends AbstractTestNGSpringContextTests{

    @Autowired
    JwtUtils jwtUtils;
    String token ;
    /*@BeforeSuite
    public void setUp() throws Exception {
        jwtUtils = new JwtUtils();
    }*/

    @Test
    public void testJwtTokenCreation() {
        token = jwtUtils.createTokenForUser(10100, "tdarex@toto.io", "tdarex@toto.io", "tdarex@toto.io", "tdarex@toto.io", new String[]{"ROLE_ADMIN", "ROLE_CREATE"});
        Assert.assertNotNull(token);
    }

    @Test
    public void testParseToken(){
        Claims claims = jwtUtils.parseUsernameFromToken(token);
        Assert.assertEquals(claims.get(JwtUtils.CLAIM_KEY_USERNAME), "tdarex@toto.io");
        String strings = (String) claims.get(JwtUtils.CLAIM_KEY_ROLES);
        Assert.assertEquals(strings, "[ ROLE_ADMIN, ROLE_CREATE ]");

        String scope = (String) claims.get(JwtUtils.CLAIM_KEY_SCOPE);
        Assert.assertEquals(strings, "[ ROLE_ADMIN, ROLE_CREATE ]");

        String user = (String) claims.get(JwtUtils.CLAIM_KEY_USER);
    }


    @Test
    public void testJwtTokenRefresh() {
        token = jwtUtils.createTokenForUser(10100, "tdarex@toto.io", "tdarex@toto.io", "tdarex@toto.io", "tdarex@toto.io", new String[]{"ROLE_ADMIN", "ROLE_CREATE"});
        System.out.println("token is : " +token);
        token = jwtUtils.refreshToken(token);
        Assert.assertNotNull(token);
    }

    @Test
    public void testRolesFromToken(){
        Claims claims = jwtUtils.parseUsernameFromToken(token);
        Assert.assertEquals(claims.get(JwtUtils.CLAIM_KEY_USERNAME), "tdarex@toto.io");

        Collection<? extends GrantedAuthority> authorities = jwtUtils.getRoles(claims);

        Assert.assertEquals(authorities.size(), 2);
    }

    @Test(expectedExceptions = ExpiredJwtException.class)
    public void testExpiryToken(){
        Claims claims = jwtUtils.parseUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2Vycy8xMDEwMDAxIiwiYXVkIjoiaHR0cDovL2Fza2x5dGljcy5pby9hcHAiLCJuYmYiOjE0OTY5NjQ5MzYsImNyZWF0ZWQiOjE0OTY5NjQ5MzYwMzAsInJvbGVzIjoiWyBST0xFX1NVUEVSX0FETUlOIF0iLCJzY29wZSI6IlsgUk9MRV9TVVBFUl9BRE1JTiBdIiwiaXNzIjoiaHR0cDovL2Fza3l0aWNzLmlvIiwidXNlck5hbWUiOiJ0ZXN0MTAxMDAwMUBtYXNzaXZlLmNvLnp6IiwiZXhwIjoxNDk2OTY1ODM2LCJ1c2VyIjoie3VzZXJOYW1lOnRlc3QxMDEwMDAxQG1hc3NpdmUuY28uenosIGxhc3RMb2dpbjpudWxsLCBmaXJzdE5hbWU6UmFtb25hLCBsYXN0TmFtZTpNY2tpbm5leSwgdXNlckltYWdlOm51bGx9IiwiaWF0IjoxNDk2OTY0OTM2LCJqdGkiOiIxMDEwMDAxLW51bGwtMTQ5Njk2NDkzNjAzMC0xMDc1In0.-6WXA5GJJH2TYXzpji-NgmjGZ0ZhE949uGx9xB76YBuegbqVFKU4MGBeh8HHlJRyCKQTATqrOREtdLJ_XgcuQw");
    }

}
