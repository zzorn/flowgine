package org.skycastle.flowgine.shape;

import org.skycastle.flowgine.FlowGine;
import org.skycastle.flowgine.resourceloader.ResourceLoader;
import org.skycastle.flowgine.utils.ResourceManagerBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager that keeps track of loaded shapes, and allows reuse.
 */
public class ShapeManager extends ResourceManagerBase<ShapeRef, Shape> {

    @Override protected Shape createResource(ShapeRef ref, ResourceLoader resourceLoader) throws Exception {
        if (ref.getPath().endsWith(".shape")) {
            final String shapeSource = resourceLoader.loadResourceAsString(ref.getPath());

            // TODO: Parse shape source

            return new Shape();
        }
        else {
            throw new IllegalStateException("Unsupported shape file type " + ref.getPath());
        }
    }


}
