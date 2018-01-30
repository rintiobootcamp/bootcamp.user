package com.bootcamp.security.filter;


import com.bootcamp.security.authentication.PagUserDetailsService;
import com.bootcamp.security.constants.SecurityToken;
import com.bootcamp.security.jwt.JwtUtils;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthenticationTokenFilter extends GenericFilterBean implements SecurityToken {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.security.login.header:access-token}") //default /
    String header;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private PagUserDetailsService pagUserDetailsService;

    public JwtAuthenticationTokenFilter() {
    }

    public JwtAuthenticationTokenFilter(JwtUtils jwtUtils, AuthenticationEntryPoint authenticationEntryPoint, CookieService cookieService, String header, PagUserDetailsService pagUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.cookieService = cookieService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.header = header;
        this.pagUserDetailsService = pagUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //if(! excludeUrl(request.getServletPath())) {

            String authToken = request.getHeader(header);
            Cookie[] cookies = request.getCookies();


            if (Strings.isNullOrEmpty(authToken) && (cookies != null && cookies.length != 0)) {
                Cookie tokenCookie = Arrays.stream(cookies).
                        filter(cookie -> cookie.getName().equalsIgnoreCase(TOKEN)).findFirst().orElse(null);
                if (tokenCookie != null)
                    authToken = tokenCookie.getValue();
            }

            if (! Strings.isNullOrEmpty(authToken)) {

                // authentication token exists
                Claims claims = jwtUtils.parseUsernameFromToken(authToken);
                if (claims != null) {
                    String username = (String) claims.get(JwtUtils.CLAIM_KEY_USERNAME);

                    if (!"customer".equals(claims.get(JwtUtils.CLAIM_KEY_USER_TYPE)))
                        throw new MalformedJwtException("Token is not valid for this context path : " + request.getContextPath());

                    logger.info("checking authentication for user " + username);

                    if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

                        authToken = jwtUtils.refreshToken(authToken);
                        // It is not compelling necessary to load the use details from the database. You could also store the information
                        // in the token and read it from it. It's up to you ;)
                        UserDetails userDetails = jwtUtils.getUserFromToken(claims);

                        // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                        // the database compellingly. Again it's up to you ;)
                        if (jwtUtils.validateToken(authToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            logger.info("authenticated user " + username + ", setting filter context");
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            Cookie refreshedCookie = cookieService.updateCookie(request.getCookies(), TOKEN, authToken);
                            response.addCookie(refreshedCookie);
                            //authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                        }
                    }
                }

            }

            chain.doFilter(request, response);
        //}
    }


    private Boolean excludeUrl(String url){
        String [] items = new String[]{
                "/",
                "^.*(/).*"
        };

        for(String item : items){
            //match with regex not inverse .. it will fail.
            if(url.matches(item)) return true;
        }
        return false;
    }

//    private Boolean excludeUrl(String url){
//        String [] items = new String[]{
//                "/",
//                "^.*(/logout).*",
//                "^.*(/categorie).*",
//                "^.*(/projet).*"
//        };
//
//        for(String item : items){
//            //match with regex not inverse .. it will fail.
//            if(url.matches(item)) return true;
//        }
//        return false;
//    }
}