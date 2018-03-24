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
    // CONSTRUCTORS
    // =========================================================================

    // =========================================================================
    // METHODS
    // =========================================================================
    @Test
    public void testWithoutTimeout() {
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
        //@formatter:off
        Chrono chrono = Chrono.startChrono();
        List<String> data = new RunAndCloseService<>("test",
                                                      2000L,
                                                      2, 
                                                      new SimpleTask(500,"1"),
                                                      new SimpleTask(10000,"2"),
                                                      new SimpleTask(1000,"3"),
                                                      new SimpleTask(700,"4")
                                                     ).run();
        //@formatter:on
        chrono.stop();
        LOGGER.info("duration : {}", chrono.getDuration());
        assertTrue(chrono.getDuration() < 2000L);
        data.forEach(m -> LOGGER.info("number : {}", m));
        assertListEquals(data, "1", "3");
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void assertListEquals(List<String> data, String... ref) {
        assertNotNull(data);
        assertEquals(data.size(), ref.length);
        for (int i = 0; i < ref.length; i++) {
            assertEquals(data.get(i), ref[i]);
        }
    }

}
