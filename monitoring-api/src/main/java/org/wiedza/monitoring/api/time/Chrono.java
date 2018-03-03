/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.time;

import java.io.Serializable;

/**
 * Chrono
 * 
 * @author patrickguillerm
 * @since 1 mars 2018
 */
public class Chrono implements Serializable {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -5814273585486352902L;

    private static final long UNDEFINE         = -1;

    private long              start;

    private long              end;

    private boolean           running;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public Chrono() {
        this(UNDEFINE, UNDEFINE, false);
    }

    private Chrono(long start, long end, boolean running) {
        this.start = start;
        this.end = end;
        this.running = running;
    }

    @Override
    public Chrono clone() {
        return new Chrono(start, end, running);
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public static Chrono startChrono() {
        return new Chrono().start();
    }

    public Chrono start() {
        start = System.currentTimeMillis();
        return this;
    }

    public Chrono snapshot() {
        return new Chrono(start, System.currentTimeMillis(), true);
    }

    public Chrono stop() {
        running = false;
        end = System.currentTimeMillis();
        return this;
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append('@').append(System.identityHashCode(this));
        builder.append("[start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);
        builder.append(", running=");
        builder.append(running);
        builder.append(", duration=");
        builder.append(getDuration());
        builder.append("ms");
        builder.append("]");
        return builder.toString();
    }

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================
    public long getDuration() {
        return end == UNDEFINE ? UNDEFINE : end - start;
    }

    public boolean isRunning() {
        return running;
    }

}
