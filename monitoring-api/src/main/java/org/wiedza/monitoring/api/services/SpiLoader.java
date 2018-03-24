/* WIEDZA
 * -----------------
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 */
package org.wiedza.monitoring.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SpiLoader
 * 
 * @author patrickguillerm
 * @since 21 mars 2018
 */
public final class SpiLoader {

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    private SpiLoader() {

    }

    // =========================================================================
    // LOAD SERVICES
    // =========================================================================
    public static <T> List<T> loadServices(Class<T> type) {
        return loadServices(type, null);
    }

    public static <T> List<T> loadServices(Class<T> type, T defaultImplementation) {
        final List<T> result = new ArrayList<>();
        ServiceLoader.load(type).forEach(result::add);

        if (defaultImplementation != null) {
            result.add(defaultImplementation);
        }
        return result;
    }

    // =========================================================================
    // LOAD SERVICE
    // =========================================================================
    public static <T> T loadService(String name, Class<T> type, T defaultImplementation) {
        T result = null;

        for (T service : loadServices(type)) {
            if (service.getClass().getSimpleName().equalsIgnoreCase(name)) {
                result = service;
                break;
            }
        }
        
        
        return result;
    }

}
