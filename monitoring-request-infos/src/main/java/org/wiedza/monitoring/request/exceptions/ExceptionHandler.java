/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.request.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.wiedza.monitoring.api.exceptions.ErrorType;
import org.wiedza.monitoring.api.loggers.Loggers;
import org.wiedza.monitoring.request.filters.ResquestInfosFilter;

/**
 * ExceptionHandler
 * 
 * @author patrickguillerm
 * @since 27 févr. 2018
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final String                  TECHNICAL_ERROR = "Technical error";


    private static final Map<Pattern, ErrorType> ERRORS_TYPES    = initErrorsType();

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private static Map<Pattern, ErrorType> initErrorsType() {
        final Map<Pattern, ErrorType> errors = new LinkedHashMap<>();
        //@formatter:off
        final BiConsumer<String, Exception> xllogConsumer     = (msg,error) -> Loggers.XLLOG.error("{} : {}",msg,error.getMessage());
        
        
        // DEV ERROR
        errors.put(regex(".*NullPointer.*"),             new ErrorType(501, "ERR-1-0", TECHNICAL_ERROR));
        errors.put(regex(".*NumberFormatException.*"),   new ErrorType(501, "ERR-1-1", TECHNICAL_ERROR));
        errors.put(regex(".*ClassCastException.*"),      new ErrorType(501, "ERR-3-2", TECHNICAL_ERROR));
        errors.put(regex(".*NoClassDefFoundError.*"),    new ErrorType(501, "ERR-3-3", TECHNICAL_ERROR));
        errors.put(regex(".*NoClassDefFoundError.*"),    new ErrorType(501, "ERR-3-4", TECHNICAL_ERROR));
        errors.put(regex(".*MethodNotFoundException.*"), new ErrorType(501, "ERR-3-5", TECHNICAL_ERROR));
        errors.put(regex(".*IllegalStateException.*"),   new ErrorType(501, "ERR-3-6", TECHNICAL_ERROR));
        
        // ENV
        errors.put(regex(".*SocketTimeoutException.*"),  new ErrorType(504, "ERR-2-0", "timeout", xllogConsumer));
        errors.put(regex(".*TimeoutException.*"),        new ErrorType(504, "ERR-2-1", "timeout", xllogConsumer));
        
        
        //@formatter:on        
        return errors;

    }

    private static Pattern regex(String regex) {
        return Pattern.compile(regex);
    }
    // =========================================================================
    // METHODS
    // =========================================================================

    @Override
    public Response toResponse(Exception error) {
        ErrorType errorType = ResquestInfosFilter.DEFAULT_ERROR;
        for (Entry<Pattern, ErrorType> entry : ERRORS_TYPES.entrySet()) {
            if (entry.getKey().matcher(error.getClass().getName()).matches()) {
                errorType = entry.getValue();
                break;
            }
        }

        if (errorType.getErrorHandler() != null) {
            errorType.getErrorHandler().accept(errorType.getMessage(), error);
        }
     
        Loggers.DEBUGLOG.error(error.getMessage(), error);
        return Response.status(errorType.getHttpCode()).entity(errorType).build();
    }

}
