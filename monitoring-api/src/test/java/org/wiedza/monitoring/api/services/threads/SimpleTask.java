/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

/**
 * SimpleTask
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public class SimpleTask implements CallableTimeoutResult<String> {

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

    public String getTimeoutResult() {
        return "timeout - " + result;
    }

}
