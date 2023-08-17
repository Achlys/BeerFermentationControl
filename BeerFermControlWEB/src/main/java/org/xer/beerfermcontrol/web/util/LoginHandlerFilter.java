package org.xer.beerfermcontrol.web.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xer.beerfermcontrol.core.bean.User;

/**
 *
 * @author Achlys
 */
public class LoginHandlerFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(LoginHandlerFilter.class);

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        User userSession = (User) request.getSession().getAttribute(WebConstants.USER);
        if (!request.getRequestURI().startsWith(request.getContextPath() + "/login") && 
                !request.getRequestURI().startsWith(request.getContextPath() + "/error") &&
                (userSession == null || userSession.getId() == null)) {
            LOGGER.error("Ha intentado acceder al path: " + request.getRequestURI() + ", sin estar logeado!!!");
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            fc.doFilter(sr, sr1);
        }
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
