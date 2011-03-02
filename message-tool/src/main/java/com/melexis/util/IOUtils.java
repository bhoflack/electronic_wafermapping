package com.melexis.util;

import static org.apache.commons.io.IOUtils.toByteArray;

public final class IOUtils {

    /**
     * Load a file from resource.
     * @param filename
     * @return a byte array containing the file
     * @throws exception
     */
    public final static byte[] resource(final String filename) throws Exception {
        return toByteArray(IOUtils.class.getClassLoader().getResourceAsStream(filename));
    }

}