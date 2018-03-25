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
public class SimpleTask implements CallableWithErrorResult<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final int     sleepTime;

    private final String  result;

    private final boolean error;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public SimpleTask(int sleepTime, String result) {
        this(sleepTime, result, false);

    }

    public SimpleTask(int sleepTime, String result, boolean error) {
        this.sleepTime = sleepTime;
        this.result = result;
        this.error = error;
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    @Override
    public String call() throws Exception {
        Thread.sleep(sleepTime);
        if (error) {
            throw new Exception("Error occurs!");
        }
        return result;
    }

    public String getTimeoutResult() {
        return "timeout - " + result;
    }

    public String getErrorResult(Exception error) {
        return "error - " + result;
    }
}
