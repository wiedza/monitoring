/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services.threads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wiedza.monitoring.api.time.Chrono;

/**
 * RunAndCLoseServiceTest
 * 
 * @author patrickguillerm
 * @since 24 mars 2018
 */
public class RunAndCloseServiceTest {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final Logger LOGGER = LoggerFactory.getLogger(RunAndCloseServiceTest.class.getSimpleName());

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testWithoutTimeout() {
        LOGGER.info("========== testWithoutTimeout ==========");
        //@formatter:off
        Chrono chrono = Chrono.startChrono();
        List<String> data = new RunAndCloseService<>("test",
                                                      5000L,
                                                      2, 
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(100,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(700,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDuration());
        assertTrue(chrono.getDuration() < 5000L);
        data.forEach(m -> LOGGER.info("number : {}", m));
        assertListEquals(data, "2", "1", "3", "4");
    }

    @Test
    public void testWithTimeout() {
        LOGGER.info("========== testWithTimeout ==========");
        //@formatter:off
        Chrono chrono = Chrono.startChrono();
        List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(10000,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(600,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDuration());
        assertTrue(chrono.getDuration() >= 2000L);
        assertTrue(chrono.getDuration() < 2050L);
        data.forEach(m -> LOGGER.info("number : {}", m));

        assertEquals("1", data.get(0));
        assertEquals("3", data.get(1));
        if ("timeout - 2".equals(data.get(2))) {
            assertEquals("timeout - 4", data.get(3));
        } else {
            assertEquals("timeout - 2", data.get(3));
        }
    }

    @Test
    public void testWithTimeoutAndErrorHandler() {
        LOGGER.info("========== testWithTimeoutAndErrorHandler ==========");
        BiFunction<Exception, Callable<String>, String> onError = (error, task) -> {
            String result = "null";
            if (task instanceof CallableWithErrorResult) {
                result = ((CallableWithErrorResult<String>) task).getTimeoutResult();
            }
            return result;
        };
        //@formatter:off
        Chrono chrono = Chrono.startChrono();
        List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      onError,
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(10000,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(700,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDuration());
        assertTrue(chrono.getDuration() >= 2000L);
        assertTrue(chrono.getDuration() < 2050L);
        data.forEach(m -> LOGGER.info("number : {}", m));
        assertEquals("1", data.get(0));
        assertEquals("3", data.get(1));
        if ("timeout - 2".equals(data.get(2))) {
            assertEquals("timeout - 4", data.get(3));
        } else {
            assertEquals("timeout - 2", data.get(3));
        }
    }

    @Test
    public void testWithSubTask() {
        LOGGER.info("========== testWithSubTask ==========");
        //@formatter:off
        Chrono chrono = Chrono.startChrono();
        List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      new ComplexTask("1"),
                                                      new ComplexTask("2")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDuration());
        data.forEach(m -> LOGGER.info("number : {}", m));
        //@formatter:off
        assertListEquals(data, "1.2 | 1.1 | error - 1.4 | 1.3",
                                "2.1 | 2.2 | 2.4 | timeout - 2.3");
        //@formatter:on
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void assertListEquals(List<String> data, String... ref) {
        assertNotNull(data);
        assertEquals(data.size(), ref.length);
        for (int i = 0; i < ref.length; i++) {
            assertEquals(ref[i], data.get(i));
        }
    }

}
