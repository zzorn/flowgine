package org.skycastle.flowgine;

import org.skycastle.flowgine.resourceloader.FileResourceLoader;
import org.skycastle.flowgine.resourceloader.ResourceLoader;
import org.skycastle.flowgine.shader.ShaderManager;
import org.skycastle.flowgine.texture.TextureManager;

/**
 * Holds various static managers.
 * Configure the managers if desired.
 */
public class FlowGine {

    public static ResourceLoader resourceLoader = new FileResourceLoader("");
    public static ShaderManager  shaderManager  = new ShaderManager();
    public static TextureManager textureManager = new TextureManager();

}
