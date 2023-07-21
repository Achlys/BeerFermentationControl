package org.xer.beerfermcontrol.web.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 *
 * @author Achlys
 */
public class BFCExceptionHandler extends SimpleMappingExceptionResolver {

    private Logger log;

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        //super.logException(ex, request);
        log.error(ex.getMessage(), ex);
    }

    @Override
    public void setWarnLogCategory(String loggerName) {
        super.setWarnLogCategory(loggerName);
        log = LogManager.getLogger(loggerName);
    }

}
