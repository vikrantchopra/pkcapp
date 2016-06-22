package com.perceptive.epm.perkolcentral.common.utils;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 21/8/12
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AjaxAwareAuthenticationEntryPointUtil extends LoginUrlAuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        boolean isAjax
                = request.getRequestURI().startsWith("/PerceptiveKolkataCentral/ajax");

        if (isAjax) {
            response.sendError(403, "Forbidden");
        } else {
            super.commence(request, response, authException);
        }
    }
}

