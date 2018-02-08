
package com.bootcamp.security.filter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            logger.debug("No failure URL set, sending 401 Unauthorized error");
            response.getWriter().write("AUTHORIZATION ERROR");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    //"Authentication Failed: " + exception.getMessage());
    }

}
