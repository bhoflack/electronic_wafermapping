package com.melexis;

import java.util.Map;

public interface ConfigDao {

    /**
     * Find the properties for the given parameter and value.
     *
     * @param parameter
     * @param parameterValue
     * @return a map containing the property name and value
     */
    Map<String, String> findForKey(final String parameter, final String parameterValue);
}