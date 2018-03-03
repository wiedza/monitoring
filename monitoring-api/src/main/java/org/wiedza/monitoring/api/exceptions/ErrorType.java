/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.exceptions;

import java.io.Serializable;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * ErrorType
 * 
 * @author patrickguillerm
 * @since 28 févr. 2018
 */
public class ErrorType implements Serializable {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -4997347726377661701L;

    private final int httpCode;
    
    private final String errorCode;
    
    private final String message;
    
    @JsonIgnore
    private final transient BiConsumer<String, Exception> errorHandler;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public ErrorType(int httpCode, String errorCode, String message) {
        this(httpCode, errorCode, message, (msg,error) -> {
        });
    }
    
    public ErrorType(int httpCode, String errorCode, String message, BiConsumer<String, Exception> errorHandler) {
        super();
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.message = message;
        this.errorHandler = errorHandler;
    }
    
    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append('@');
        builder.append(System.identityHashCode(this));
        builder.append("[httpCode=").append(httpCode);
        builder.append(", errorCode=").append(errorCode);
        builder.append(", message=").append(message);
        builder.append("]");
        return builder.toString();
    }
    
    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public int getHttpCode() {
        return httpCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public BiConsumer<String, Exception> getErrorHandler() {
        return errorHandler;
    }
    
}
