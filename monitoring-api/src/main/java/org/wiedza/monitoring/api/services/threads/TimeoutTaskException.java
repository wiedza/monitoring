/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

/**
 * TimeoutTaskException
 * 
 * @author patrickguillerm
 * @since 25 mars 2018
 */
public class TimeoutTaskException extends Exception {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = 4991745549592164932L;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    public TimeoutTaskException() {
        super();
    }

    public TimeoutTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutTaskException(String message) {
        super(message);
    }

    public TimeoutTaskException(Throwable cause) {
        super(cause);
    }
}
