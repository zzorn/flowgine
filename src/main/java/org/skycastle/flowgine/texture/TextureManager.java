package org.skycastle.flowgine.texture;

import org.skycastle.flowgine.resourceloader.ResourceLoader;
import org.skycastle.flowgine.shape.ShapeManager;
import org.skycastle.flowgine.utils.ResourceManagerBase;

/**
 * Handles loading of textures.
 */
// TODO: Should maybe do some caching of loaded images?  Maybe do that in a separate image manager / loader.
public class TextureManager extends ResourceManagerBase<TextureRef, Texture> {

    @Override protected Texture createResource(TextureRef ref, ResourceLoader resourceLoader) throws Exception {
        // TODO: Load & generate.  Look at path suffix to identify file type (png, jpg, procedural texture etc)

        return null;
    }


}
