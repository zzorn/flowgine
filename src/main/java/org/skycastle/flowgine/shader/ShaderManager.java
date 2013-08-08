package org.skycastle.flowgine.shader;

import org.skycastle.flowgine.FlowGine;
import org.skycastle.flowgine.resourceloader.ResourceLoader;
import org.skycastle.flowgine.utils.ResourceManagerBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of loaded shaders, and ensures there is only one of each loaded at a time.
 * Frees unused shaders from the graphics card.
 */
public class ShaderManager extends ResourceManagerBase<ShaderRef, Shader> {

    @Override protected Shader createResource(ShaderRef ref, ResourceLoader resourceLoader) throws Exception {
        final String vertexSource   = resourceLoader.loadResourceAsString(ref.getVertexShaderPath());
        final String fragmentSource = resourceLoader.loadResourceAsString(ref.getFragmentShaderPath());

        return new Shader(ref.getVertexShaderPath(), vertexSource,
                          ref.getFragmentShaderPath(), fragmentSource);
    }

}
