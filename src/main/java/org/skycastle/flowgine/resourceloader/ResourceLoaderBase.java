package org.skycastle.flowgine.resourceloader;

import org.skycastle.flowgine.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for resource loaders.
 */
public abstract class ResourceLoaderBase implements ResourceLoader {

    private String basePath = "";

    protected ResourceLoaderBase(String basePath) {
        this.basePath = basePath;
    }

    public final InputStream loadResource(String resourcePath) {
        final String path = basePath + resourcePath;
        try {
            return doLoadResource(path);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Problem loading resource with the path '"+path+"': " + e.getMessage(), e);
        }
    }

    public String loadResourceAsString(String resourcePath) {
        try {
            return FileUtils.readStream(loadResource(resourcePath));
        } catch (IOException e) {
            throw new IllegalArgumentException("Problem reading text resource with the path '"+(basePath + resourcePath)+"': " + e.getMessage(), e);
        }
    }

    protected abstract InputStream doLoadResource(String resourcePath) throws Exception;
}
