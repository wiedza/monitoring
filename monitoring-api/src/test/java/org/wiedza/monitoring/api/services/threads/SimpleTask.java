/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import java.util.concurrent.Callable;

/**
 * SimpleTask
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public class SimpleTask implements Callable<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int    sleepTime;

    private final String result;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleTask(int sleepTime, String result) {
        super();
        this.sleepTime = sleepTime;
        this.result = result;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String call() throws Exception {
        Thread.sleep(sleepTime);
        return result;
    }

}
