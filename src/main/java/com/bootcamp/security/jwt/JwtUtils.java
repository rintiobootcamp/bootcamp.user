
package com.bootcamp.security.jwt;

import com.bootcamp.security.PagSecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Configuration
public class JwtUtils implements Serializable{

    private static Logger logger = LogManager.getLogger(JwtUtils.class);

    private static final long serialVersionUID = -3301605591108950415L;

    public static final String CLAIM_KEY_USERNAME = "userName";
    public static final String CLAIM_KEY_USER = "user";
    public static final String CLAIM_KEY_LASTLOGIN = "lastLogin";
    public static final String CLAIM_KEY_FIRSTNAME = "firstName";
    public static final String CLAIM_KEY_LASTNAME = "lastName";
    public static final String CLAIM_KEY_USERIMAGE = "userImage";
    public static final String CLAIM_KEY_AUDIENCE = "audience";
    public static final String CLAIM_KEY_CREATED = "created";
    public static final String CLAIM_KEY_ROLES = "roles";
    public static final String CLAIM_KEY_SCOPE = "scope";
    public static final String CLAIM_KEY_USER_TYPE = "userType";

    private static final String CLAIM_VALUE_ISSUER = "http://askytics.io";
    private static final String CLAIM_VALUE_AUDIENCE = "http://asklytics.io/app";

    private static final String PREFIX_USERS = "users/";
    private static final String CLAIM_KEY_TENANT_ID="syncId";
    private static final String CLAIM_KEY_CUSTOMER_ID="cid";


    //@Value("${app.security.nbf.time.limit.sec:0}") //default 0
    private int DEFAULT_NBF_TIME_LIMIT_SEC=0;
    //@Value("${app.security.exp.time.limit.min:15}") //default 15 min
    private int DEFAULT_EXP_TIME_LIMIT_MIN=10;
   // @Value("${app.security.exp.time.clock.skew.sec:30}") //default /api/login/**
    private int DEFAULT_EXP_TIME_CLOCK_SKEW_SEC=30;

    //TODO - CRITICAL REMOVE HSC of SECRET *********
    private static final String SECRET = "ASKLYTICSKEY";

    public Claims parseUsernameFromToken(String token){
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims;
        } catch (JwtException e) {
            logger.error("Jwt Exception is thrown : " + e);
            throw e;
        }
    }

    private String generateToken(Map<String, Object> claims, Integer userId ) {
        return generateToken(claims, userId, DEFAULT_EXP_TIME_LIMIT_MIN, DEFAULT_NBF_TIME_LIMIT_SEC);
    }

    private String generateToken(Map<String, Object> claims, Integer userId, int expTimeLimitInMinutes, int nbfTimeLimitSec) {
        //TimeZone.getTimeZone("UTC");
        // All tokens must use UTC time zone to make it agnostic to system time zone ; apps can be deployed across datacenters across different time zones

        logger.debug("Going to generate token for user after passing all validation");
        Date issueTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueTime);
        calendar.add(Calendar.SECOND, nbfTimeLimitSec);
        Date notBeforeTime = calendar.getTime();
        Date expiryTime = generateExpirationDate(expTimeLimitInMinutes);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(PREFIX_USERS + userId.toString())
                .setIssuer(CLAIM_VALUE_ISSUER)
                .setAudience(CLAIM_VALUE_AUDIENCE)
                .setIssuedAt(issueTime)
                .setExpiration(expiryTime)
                .setNotBefore(notBeforeTime)
                .setId(userId.toString() + "-" +
                        claims.get(CLAIM_VALUE_ISSUER) + "-" +
                        String.valueOf(issueTime.getTime()) + "-" +
                        String.format("%04d", new Random().nextInt(10000)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String createTokenForUser(Integer userId, String username, String[] roles) {
    //TODO: Need to externalize time using property jwt_exp_time_limit_sec_mlaas
    // TODO: Need to externalize time using property jtw_nbf_time_limit_sec_mlaas
        logger.debug("Going to create a token for user");
        return createTokenForUser(userId, username, roles, DEFAULT_EXP_TIME_LIMIT_MIN, DEFAULT_NBF_TIME_LIMIT_SEC);
    }

    public String createTokenForUser(Integer userId, String username, String lastname, String firstname, String lastLogin,
                                      String[] roles) {
        //TODO: Need to externalize time using property jwt_exp_time_limit_sec_mlaas
        // TODO: Need to externalize time using property jtw_nbf_time_limit_sec_mlaas
        logger.debug("Going to create a token for user");
        return createTokenForUser(userId, username,  lastname, firstname, lastLogin, roles, DEFAULT_EXP_TIME_LIMIT_MIN, DEFAULT_NBF_TIME_LIMIT_SEC);
    }

    public String createTokenForUser(Integer userId, String username, String lastname, String firstname, String lastLogin, String[] roles, Integer expiryInMin) {
        //TODO: Need to externalize time using property jwt_exp_time_limit_sec_mlaas
        // TODO: Need to externalize time using property jtw_nbf_time_limit_sec_mlaas
        logger.debug("Going to create a token for user");
        return createTokenForUser(userId, username,  lastname, firstname, lastLogin,   roles, expiryInMin, DEFAULT_NBF_TIME_LIMIT_SEC);
    }

    /**
     *
     * @param userId
     * @param username
     * @param roles the string values of the enum com.asklytics.dao.enums.UserRole
     * @return
     */
    private String createTokenForUser(Integer userId, String username, String[] roles, int expTimeLimitInMinutes, int nbfTimeLimitSec) {
        if(null==roles || 0==roles.length) {
            logger.error("Failed Creating Token for User. userId:" + userId + " Username:" + username + " Reason: roles is null or empty");
            throw new InternalAuthenticationServiceException("User does not have roles. roles: null or empty");
        }
        if (expTimeLimitInMinutes < 1) {
           expTimeLimitInMinutes = DEFAULT_EXP_TIME_LIMIT_MIN;
        }
        if(nbfTimeLimitSec < 0 ) {
            nbfTimeLimitSec = DEFAULT_NBF_TIME_LIMIT_SEC;
        }
        StringBuilder sbRoles = new StringBuilder("[ ");

        for (int i=0; i < roles.length; i++) {
            sbRoles.append(roles[i]) ;
            if (i != (roles.length - 1)) {
                sbRoles.append(", ");
            }
        }
        sbRoles.append(" ]");

        //TODO - add lastlogin nad userImage to tenantUser entity

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ROLES, sbRoles.toString());
        claims.put(CLAIM_KEY_SCOPE, sbRoles.toString());
        claims.put(CLAIM_KEY_USER, "");
        claims.put(CLAIM_KEY_USER_TYPE, "resource");

        return generateToken(claims, userId, expTimeLimitInMinutes, nbfTimeLimitSec);
    }


    private String createTokenForUser(Integer userId, String username, String lastname, String firstname, String lastLogin,   String[] roles, int expTimeLimitInMinutes, int nbfTimeLimitSec) {
        if(null==roles || 0==roles.length) {
            logger.error("Failed Creating Token for User. userId:" + userId + " Username:" + username + " Reason: roles is null or empty");
            throw new InternalAuthenticationServiceException("User does not have roles. roles: null or empty");
        }
        if (expTimeLimitInMinutes < 1) {
            expTimeLimitInMinutes = DEFAULT_EXP_TIME_LIMIT_MIN;
        }
        if(nbfTimeLimitSec < 0 ) {
            nbfTimeLimitSec = DEFAULT_NBF_TIME_LIMIT_SEC;
        }
        StringBuilder sbRoles = new StringBuilder("[ ");

        for (int i=0; i < roles.length; i++) {
            sbRoles.append(roles[i]) ;
            if (i != (roles.length - 1)) {
                sbRoles.append(", ");
            }
        }
        sbRoles.append(" ]");

        //TODO - add lastlogin nad userImage to tenantUser entity
        StringBuilder user = new StringBuilder("{");
        user.append(CLAIM_KEY_USERNAME + ":"); user.append(username); user.append(", ");
        user.append(CLAIM_KEY_LASTLOGIN + ":"); user.append(lastLogin); user.append(", ");
        user.append(CLAIM_KEY_FIRSTNAME + ":"); user.append(firstname); user.append(", ");
        user.append(CLAIM_KEY_LASTNAME + ":"); user.append(lastname); user.append(", ");
        user.append("}");

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ROLES, sbRoles.toString());
        claims.put(CLAIM_KEY_SCOPE, sbRoles.toString());
        claims.put(CLAIM_KEY_USER, user.toString());
        claims.put(CLAIM_KEY_USER_TYPE, "customer");

        return generateToken(claims, userId, expTimeLimitInMinutes, nbfTimeLimitSec);
    }


    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw e;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return generateExpirationDate(DEFAULT_EXP_TIME_LIMIT_MIN);
    }

    private Date generateExpirationDate(int expTimeLimitInMinutes) {
        return new Date(System.currentTimeMillis() + (expTimeLimitInMinutes * 60 * 1000) + (DEFAULT_EXP_TIME_CLOCK_SKEW_SEC * 1000));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /*private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }*/

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {

            //signature algorithm is identified via the alg property located in the header section of the JWT
            // Jwts.parse verifies the token; will generate SignatureException if verification fails
            final Claims claims = parseUsernameFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims, Integer.valueOf(claims.getSubject().split("/")[1]));
        } catch (Exception e) {
            logger.debug("Failed to refresh token:" + token, e);
            //returning expired token so that the rest of the app does not fail
            return token;
        }

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (userDetails instanceof PagSecurityUser) {
            PagSecurityUser user = (PagSecurityUser) userDetails;

            final Claims claims = parseUsernameFromToken(token);
            //final Date expiration = getExpirationDateFromToken(token);
            return (claims.get(CLAIM_KEY_USERNAME).equals(user.getUsername())
                    && !isTokenExpired(token));
        } else {
            User user = (User) userDetails;
            final Claims claims = parseUsernameFromToken(token);
            //final Date expiration = getExpirationDateFromToken(token);
            return (claims.get(CLAIM_KEY_USERNAME).equals(user.getUsername())
                    && !isTokenExpired(token));
        }
    }

    public Collection<? extends GrantedAuthority> getRoles(Claims claims){
        String roles = (String) claims.get(CLAIM_KEY_ROLES);

        if (roles == null || roles.length()==0)
            return new ArrayList<SimpleGrantedAuthority>();

        String[] tokenizer = roles.substring(2, roles.length() - 2).replaceAll("\\s+","").split(",");

        return Arrays.stream(tokenizer).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public PagSecurityUser getUserFromToken(Claims claims){
        String subject = claims.getSubject();
        Integer userId = Integer.valueOf(subject.substring(6));
        String user = ((String) claims.get(CLAIM_KEY_USER)).substring(0, ((String) claims.get(CLAIM_KEY_USER)).length() - 1).replaceAll("\\s+","");
        String[] tokenizer = user.split(",");

        return new PagSecurityUser((String) claims.get(JwtUtils.CLAIM_KEY_USERNAME),
                "",
                getRoles(claims),
                userId,
                tokenizer[2].substring(CLAIM_KEY_FIRSTNAME.length()+1),
                tokenizer[3].substring(CLAIM_KEY_LASTNAME.length()+1)
        );
    }


    public User getResourceUserFromToken(Claims claims){
        return new User((String) claims.get(JwtUtils.CLAIM_KEY_USERNAME), "", getRoles(claims));
    }
}
