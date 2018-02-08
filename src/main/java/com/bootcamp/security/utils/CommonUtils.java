
package com.bootcamp.security.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CommonUtils {
    private static final Log logger = LogFactory.getLog(CommonUtils.class);

    private CommonUtils() {
    }

    public static StringBuilder extractHttpRequestData(HttpServletRequest request, StringBuilder sb) {

        if(null==sb) {
            sb = new StringBuilder();
        }
        try {
            sb.append("URI=").append(request.getRequestURI()).append("\n");
            sb.append("URL=").append(request.getRequestURL()).append("\n");
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String) headerNames.nextElement();
                sb.append("Header Name - ").append(headerName).append(", Value - ").
                        append(request.getHeader(headerName)).append("\n");
            }
            Enumeration params = request.getParameterNames();
            while(params.hasMoreElements()){
                String paramName = (String)params.nextElement();
                sb.append("Parameter Name - ").append(paramName).append(", Value - ").
                        append(request.getParameter(paramName)).append("\n");
            }

            Cookie[] cookies = request.getCookies();
            if (cookies==null || cookies.length==0) {
                return sb.append("Cookies - none");
            }
            else {
                sb.append("Cookies - ");
                for (Cookie cookie : cookies){
                    sb.append("\n   cookie name:").append(cookie.getName()).append("  ").append("value:").append(cookie.getValue()).append("  ")
                            .append("domain:").append(cookie.getDomain()).append("  ")
                            .append("path:").append(cookie.getPath()).append("  ")
                            .append("maxAge:").append(cookie.getMaxAge()).append("  ")
                            .append("secure:").append(cookie.getSecure()).append("  ")
                            .append("version:").append(cookie.getVersion()).append("  ")
                            .append("isHttpOnly:").append(cookie.isHttpOnly()).append("  ")
                            .append("comment:").append(cookie.getComment()).append("  ");
                }
            }
        }catch (Exception e) {
            logger.error("failed to extract header and param from HttpRequest", e);
        }
        return sb;
    }

    public static boolean postAuthenticationException(AuthenticationEntryPoint authenticationEntryPoint, HttpServletRequest request, HttpServletResponse response, String authToken, JwtException e) throws IOException, ServletException {
        if (e instanceof ExpiredJwtException){
            logger.error("security error: Token expired. authToken=" + authToken, e);
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("Token expired."));
            return true;
        }
        if (e instanceof MalformedJwtException){
            logger.error("security error: Token is invalid. authToken=" + authToken, e);
            authenticationEntryPoint.commence(request, response, new InvalidCookieException("Token is not valid in the cookie. authToken=" + authToken));
            return true;
        }
        logger.error("security error: Token validation failed. authToken=" + authToken, e);
        return false;
    }
}
