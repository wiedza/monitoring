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
    public static final Logger XLLOG        = LoggerFactory.getLogger("XLLOG");

    public static final Logger DEBUGLOG     = LoggerFactory.getLogger("DEBUGLOG");

    public static final Logger SYSTEMLOG    = LoggerFactory.getLogger("SYSTEMLOG");

    public static final Logger AUDITOUTLINE = LoggerFactory.getLogger("AUDITOUTLINE");

    public static final Logger IOLOG        = LoggerFactory.getLogger("IOLOG");

    public static final Logger PARTNERLOG   = LoggerFactory.getLogger("PARTNERLOG");

    public static final Logger SECURITY     = LoggerFactory.getLogger("SECURITY");

    public static final Logger SCRIPTS      = LoggerFactory.getLogger("SCRIPTS");

    public static final Logger BOOTSTRAP    = LoggerFactory.getLogger("BOOTSTRAP");

    public static final Logger CHRONOLOG    = LoggerFactory.getLogger("CHRONOLOG");

}
