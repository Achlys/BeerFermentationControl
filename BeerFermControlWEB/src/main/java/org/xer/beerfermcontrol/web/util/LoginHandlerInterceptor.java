package org.xer.beerfermcontrol.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xer.beerfermcontrol.core.bean.User;

/**
 *
 * @author Achlys
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LogManager.getLogger(LoginHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User userSession = (User) request.getSession().getAttribute(WebConstants.USER);
        if (!request.getRequestURI().startsWith(request.getContextPath() + "/login") && userSession == null) {
            LOGGER.error("Ha intentado acceder al path: " + request.getRequestURI() + ", sin estar logeado!!!");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        } else {
            return true;
        }
    }

}
