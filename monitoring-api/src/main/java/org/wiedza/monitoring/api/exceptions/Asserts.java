/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.exceptions;

/**
 * Asserts
 * 
 * @author patrickguillerm
 * @since 21 mars 2018
 */
public final class Asserts {

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private Asserts() {

    }

    // =========================================================================
    // NULL / NOT NULL
    // =========================================================================
    public static void isNull(final Object... values) {
        isNotNull(null, values);
    }

    public static void isNull(String msg, final Object... values) {
        for (final Object value : values) {
            if (value != null) {
                throw new IllegalArgumentException(msg == null ? "objects must be null!" : msg);
            }
        }
    }
    
    public static void isNotNull(final Object... values) {
        isNotNull(null, values);
    }

    public static void isNotNull(String msg, final Object... values) {
        for (final Object value : values) {
            if (value == null) {
                throw new IllegalArgumentException(msg == null ? "objects mustn't be null!" : msg);
            }
        }
    }
    
    
}
