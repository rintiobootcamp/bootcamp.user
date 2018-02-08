package com.bootcamp.security.filter;

import com.bootcamp.security.constants.SecurityToken;
import com.bootcamp.security.jwt.JwtUtils;
import com.bootcamp.security.utils.CommonUtils;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
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

public class AlResourceAuthenticationFilter extends GenericFilterBean implements SecurityToken {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CookieService cookieService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(!excludeUrl(request.getServletPath())) {
            String authToken = request.getHeader(TOKEN);
            Cookie[] cookies = request.getCookies();

            if (Strings.isNullOrEmpty(authToken) && (cookies != null && cookies.length != 0)) {
                Cookie tokenCookie = Arrays.stream(cookies).
                        filter(cookie -> cookie.getName().equalsIgnoreCase(TOKEN)).findFirst().orElse(null);
                if (tokenCookie != null) {
                    authToken = tokenCookie.getValue();
                }
            }

            if (Strings.isNullOrEmpty(authToken)) {
                logger.error("security error: Attempting to access restricted resource without access token in cookie. "
                        + "InvalidCookieException(\"Cookie Does not contain access token\") HttpSeveletRequest dump is below\n"
                        + CommonUtils.extractHttpRequestData(request, new StringBuilder()).toString());
                authenticationEntryPoint.commence(request, response, new InvalidCookieException("Cookie does not contain access token"));
                return;
            }

            // authentication token exists
            if (!Strings.isNullOrEmpty(authToken)) {
                try {
                    Claims claims = jwtUtils.parseUsernameFromToken(authToken);
                    if (claims != null) {
                        String username = (String) claims.get(JwtUtils.CLAIM_KEY_USERNAME);
                        logger.info("User name "+username);
                        logger.info("Avant if "+claims.get(JwtUtils.CLAIM_KEY_USER_TYPE));
                        if (!"customer".equals(claims.get(JwtUtils.CLAIM_KEY_USER_TYPE))) {
                            logger.info("Intérieur if "+claims.get(JwtUtils.CLAIM_KEY_USER_TYPE));
                            throw new MalformedJwtException("Token is not valid for this context path : " + request.getContextPath());
                        }

                        logger.info("checking authentication für user " + username);

                        if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

                            authToken = jwtUtils.refreshToken(authToken);
                            // It is not compelling necessary to load the use details from the database. You could also store the information
                            // in the token and read it from it. It's up to you ;)
                            UserDetails userDetails = jwtUtils.getResourceUserFromToken(claims);

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
                } catch (JwtException e) {
                    CommonUtils.postAuthenticationException(authenticationEntryPoint, request, response, authToken, e);
                    return;
                }

            }
        }

        chain.doFilter(request, response);
    }

    private Boolean includeUrl(String url) {
        String[] items = new String[]{
            "^.*(/).*"
        };

        for (String item : items) {
            //match with regex not inverse .. it will fail.
            if (url.matches(item)) {
                return true;
            }
        }

        return false;
    }

    private Boolean excludeUrl(String url) {
        String[] items = new String[]{
            "^.*(/api/login).*"
        };

        for (String item : items) {
            //match with regex not inverse .. it will fail.
            if (url.matches(item)) {
                return true;
            }
        }
        return false;
    }
}
