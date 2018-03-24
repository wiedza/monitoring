/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.request.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.wiedza.monitoring.api.exceptions.ErrorType;
import org.wiedza.monitoring.api.loggers.Loggers;

/**
 * ResquestInfosFilter
 * 
 * @author patrickguillerm
 * @since 1 mars 2018
 */
public class ResquestInfosFilter implements Filter {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    //@formatter:off
    public static final ErrorType               DEFAULT_ERROR   = new ErrorType(500, "ERR-0_0", "unknow error",
                                                                       (msg, error) -> {Loggers.DEBUGLOG.error(error.getMessage(),error);});
    //@formatter:on

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        RequestInfosContext.build((HttpServletRequest) request);
        ErrorType error = null;
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException exception) {
            error = resolveErrorType(exception);
            throw exception;
        } finally {
            RequestInfosContext.currentInstance().stopChrono();
            if (error == null) {
                Loggers.AUDITOUTLINE.info("chrono : {}ms", RequestInfosContext.currentInstance().getChrono().getDuration());
            } else {
                Loggers.AUDITOUTLINE.error("chrono : {}ms", RequestInfosContext.currentInstance().getChrono().getDuration());
            }
        }
    }

    // =========================================================================
    // RESOLVER
    // =========================================================================
    private ErrorType resolveErrorType(Exception exception) {
        ErrorType result = DEFAULT_ERROR;
        // TODO : invoke SPI resolver provider
        return result;
    }

}
