/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.request.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.wiedza.monitoring.api.time.Chrono;

/**
 * RequestInfosContext
 * 
 * @author patrickguillerm
 * @since 1 mars 2018
 */
public class RequestInfosContext {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static ThreadLocal<RequestInfosContext> localThreadCxt = new ThreadLocal<>();

    private final ResquestInfos                     infos;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    /* package */ static void build(HttpServletRequest request) {
        final RequestInfosContext instance = new RequestInfosContext(request);
        localThreadCxt.set(instance);
    }

    private RequestInfosContext(HttpServletRequest request) {
        infos = new ResquestInfos(request);
        initializeMdc();

    }

    public static RequestInfosContext currentInstance() {
        RequestInfosContext result = localThreadCxt.get();
        if (result == null) {
            build(null);
            result = localThreadCxt.get();
        }
        return result;
    }
    // =========================================================================
    // METHODS
    // =========================================================================
    /* package */void stopChrono() {
        this.infos.stopChrono();
    }

    // =========================================================================
    // MDC
    // =========================================================================
    private void initializeMdc() {

        setMdc("service", infos.getService());
        setMdc("correlationId", infos.getCorrelationId());
        setMdc("requestId", infos.getRequestId());
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private void setMdc(String key, Object value) {
        if (value != null) {
            MDC.put(key, String.valueOf(value));
        }
    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public ResquestInfos getInfos() {
        return infos;
    }

    public String getCorrelationId() {
        return infos.getCorrelationId();
    }

    public String getRequestId() {
        return infos.getRequestId();
    }

    public Chrono getChrono() {
        return infos.getChrono();
    }

}
