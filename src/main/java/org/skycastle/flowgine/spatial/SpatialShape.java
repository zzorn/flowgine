package org.skycastle.flowgine.spatial;

import org.skycastle.flowgine.Generator;
import org.skycastle.flowgine.Shape;
import org.skycastle.flowgine.utils.Shader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class SpatialShape extends AbstractSpatial {

    private final Generator generator;
    private Map<String, Float> generatorParameters = new LinkedHashMap<String, Float>();
    private Shape shape = new Shape();
    private Shader shader;

    public SpatialShape(Generator generator, Map<String, Float> parameters) {
        this.generator = generator;
        this.generatorParameters.putAll(parameters);
    }

    @Override protected void doCreate() {
        //shaderName = generator.generateShape(generatorParameters, shape, shaderParameters);
        // TODO: Also get & load texture(s)
        // TODO: Load shader using shader pool
    }

    @Override protected void doUpdate(double deltaSeconds, double gameTimeSeconds) {
        //shaderName = generator.updateShape(generatorParameters, shape, shaderParameters, shaderName);

        // TODO: Release and rebind shader if necessary - Use shader pool

        // TODO: Update vertex object if changed
    }

    @Override protected void doRender() {
        // TODO: Bind VBO and index buffer and textures and shader
        // TODO: Render triangles
    }

    @Override protected void doDelete() {
        // TODO: Unbind shape
        // TODO: Release textures
        // TODO: Release shader using shader pool
    }
}
