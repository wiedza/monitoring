/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import java.util.concurrent.Callable;

/**
 * CallableTimeout
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public interface CallableWithErrorResult<V> extends Callable<V> {
    default V getTimeoutResult() {
        return null;
    }

    default V getErrorResult(Exception error) {
        return null;
    }
}
