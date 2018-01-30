
package com.bootcamp.security.filter;


import com.bootcamp.security.constants.SecurityToken;
import com.bootcamp.security.jwt.JwtUtils;
import com.bootcamp.security.PagSecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter implements SecurityToken {
    private static final Logger logger = LogManager.getLogger(JwtLoginFilter.class);

    @Autowired
    CookieService cookieService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    public JwtLoginFilter(String url, AuthenticationManager authManager, AuthenticationFailureHandler authenticationFailureHandler) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
        String authToken = req.getHeader(TOKEN);
        Cookie[] cookies = req.getCookies();
        JwtAuthenticationRequest creds = null;

        if(Strings.isNullOrEmpty(authToken) &&(cookies != null && cookies.length != 0)){
            Cookie  tokenCookie = Arrays.stream(cookies).
                    filter(cookie -> cookie.getName().equalsIgnoreCase(TOKEN)).findFirst().orElse(null);
            if(tokenCookie != null ) {
                authToken = tokenCookie.getValue();
                if (!Strings.isNullOrEmpty(authToken)) {
                    try {
                        Claims claims = jwtUtils.parseUsernameFromToken(authToken);
                        PagSecurityUser pagSecurityUser = jwtUtils.getUserFromToken(claims);
                        Authentication authenticationResult = new UsernamePasswordAuthenticationToken(pagSecurityUser, pagSecurityUser.getPassword(), pagSecurityUser.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

                        return authenticationResult;
                    } catch (JwtException e) {
                        logger.error("Not able to validate token. Checking for username/password");
                        if (e instanceof ExpiredJwtException) {
                            creds = new ObjectMapper().readValue(req.getInputStream(), JwtAuthenticationRequest.class);
                            if(creds==null) {
                                logger.error("Token is expired " + e);
                                throw new RuntimeException("Token is expired");
                            }
                        } if (e instanceof MalformedJwtException){
                            creds = new ObjectMapper().readValue(req.getInputStream(), JwtAuthenticationRequest.class);
                            if(creds == null) {
                                logger.error("Token is invalid");
                                throw new InvalidCookieException("Token is not valid in the cookie");
                            }

                        }
                    }
                }
            }
        }
        if (creds == null) {
            creds = new ObjectMapper().readValue(req.getInputStream(), JwtAuthenticationRequest.class);
        }
        final Authentication authentication = getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword(),
                        Collections.emptyList()
                )
        );

        User user = (User) authentication.getPrincipal();
        /*if (user instanceof PagSecurityUser) {
            final PagSecurityUser pagSecurityUser = (PagSecurityUser) authentication.getPrincipal();
            if (pagSecurityUser.getPasswordExpired() != null && pagSecurityUser.getPasswordExpired()) {
                //extract reason code by tokenize with delimiter (,) e.g password expired, 1 here 1 is the expiry reason
                throw new CredentialsExpiredException("Password Expired," + userDetails.getExpiryReason());
            }
        }*/

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException, ServletException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof PagSecurityUser) {
            // Reload password post-filter so we can generate token
            final PagSecurityUser userDetails = (PagSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            final String token = jwtUtils.createTokenForUser(userDetails.getUserId(), userDetails.getUsername(),
                    userDetails.getLastName(),
                    userDetails.getFirstName(),
                    userDetails.getUsername(),
                    roles.toArray(new String[roles.size()]));

            HttpStatus httpStatus = HttpStatus.OK;

            res.addCookie(cookieService.setAlAccessTokenCookie(token));
        } else {
            User resourceUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Collection<GrantedAuthority> authorities = resourceUser.getAuthorities();
            List<String> roles = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authorities){
                roles.add(grantedAuthority.getAuthority());
            }
            final  String token = jwtUtils.createTokenForUser(resourceUser.hashCode(), user.getUsername(), roles.toArray(new String[roles.size()]));

            HttpStatus httpStatus = HttpStatus.OK;

            res.addCookie(cookieService.setAlAccessTokenCookie(token));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString(), failed);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + getFailureHandler());
        }

        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
