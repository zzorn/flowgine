package org.skycastle.flowgine.resourceloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Resource loader that loads resources from files.
 */
public class FileResourceLoader extends ResourceLoaderBase {

    public FileResourceLoader(String basePath) {
        super(basePath);
    }

    @Override protected InputStream doLoadResource(String resourcePath) throws Exception {
        return new FileInputStream(resourcePath);
    }

}
