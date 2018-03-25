/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loggers
 * 
 * @author patrickguillerm
 * @since 27 févr. 2018
 */
public final class Loggers {

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    private Loggers() {
    }

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    public final static Logger XLLOG        = LoggerFactory.getLogger("XLLOG");

    public final static Logger DEBUGLOG     = LoggerFactory.getLogger("DEBUGLOG");

    public final static Logger SYSTEMLOG    = LoggerFactory.getLogger("SYSTEMLOG");

    public final static Logger AUDITOUTLINE = LoggerFactory.getLogger("AUDITOUTLINE");

    public final static Logger IOLOG        = LoggerFactory.getLogger("IOLOG");

    public final static Logger PARTNERLOG   = LoggerFactory.getLogger("PARTNERLOG");

    public final static Logger SECURITY     = LoggerFactory.getLogger("SECURITY");

    public final static Logger SCRIPTS      = LoggerFactory.getLogger("SCRIPTS");

    public final static Logger BOOTSTRAP    = LoggerFactory.getLogger("BOOTSTRAP");

    public final static Logger CHRONOLOG    = LoggerFactory.getLogger("CHRONOLOG");

}
