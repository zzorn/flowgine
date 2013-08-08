package org.skycastle.flowgine.resourceloader;

import java.io.InputStream;

/**
 * Streams resources based on resource paths.
 */
public interface ResourceLoader {

    /**
     * Retrieves a named resource.
     *
     * @param resourcePath path to retrieve the resource from
     * @return input stream for the specified resource, should be closed by the caller.
     * @throws IllegalArgumentException if the resource could not be found.
     */
    InputStream loadResource(String resourcePath);

    /**
     * Retrieves a named UTF-8 text resource.
     *
     * @param resourcePath path to retrieve the resource from
     * @return input stream for the specified resource, should be closed by the caller.
     * @throws IllegalArgumentException if the resource could not be found.
     */
    String loadResourceAsString(String resourcePath);

}
