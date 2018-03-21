/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.exceptions;

/**
 * ExceptionResolver
 * @author patrickguillerm
 * @since 21 mars 2018
 */
public interface ExceptionResolverSPI {
    ErrorType resolve(Exception exception);
}
