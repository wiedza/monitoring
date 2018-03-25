/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ComplexTask
 * 
 * @author patrickguillerm
 * @since 25 mars 2018
 */
public class ComplexTask implements CallableWithErrorResult<String> {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String value;

    // =========================================================================
    // METHODS
    // =========================================================================
    public ComplexTask(String value) {
        this.value = value;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String call() throws Exception {
        List<Callable<String>> tasks = buildTask();
        //@formatter:off
        List<String> data = new RunAndCloseService<>("test" + value,
                                                     1500L,
                                                     2,
                                                     tasks).run();
        //@formatter:on
        return  String.join(" | ", data);
    }

    private List<Callable<String>> buildTask() {
        final List<Callable<String>> result = new ArrayList<>();

        if ("1".equals(value)) {
            result.add(new SimpleTask(500, "1.1"));
            result.add(new SimpleTask(400, "1.2"));
            result.add(new SimpleTask(1000, "1.3"));
            result.add(new SimpleTask(500, "1.4", true));
        } else {
            result.add(new SimpleTask(100, value + ".1"));
            result.add(new SimpleTask(500, value + ".2"));
            result.add(new SimpleTask(2000, value + ".3"));
            result.add(new SimpleTask(100, value + ".4"));
        }
        return result;
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    @Override
    public String getTimeoutResult() {
        return "null";
    }
}
