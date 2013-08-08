package org.skycastle.flowgine.resourceloader;

import java.io.InputStream;

/**
 * Resource loader that loads embedded jar resources from the classpath.
 */
public class ClasspathResourceLoader extends ResourceLoaderBase {
    public ClasspathResourceLoader(String basePath) {
        super(basePath);
    }

    @Override protected InputStream doLoadResource(String resourcePath) throws Exception {
        return ClasspathResourceLoader.class.getResourceAsStream("/" + resourcePath.replace('\\', '/'));
    }
}
