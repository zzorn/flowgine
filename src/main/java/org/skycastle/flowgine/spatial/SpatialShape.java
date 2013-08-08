package org.skycastle.flowgine.spatial;

import org.skycastle.flowgine.FlowGine;
import org.skycastle.flowgine.shape.Shape;
import org.skycastle.flowgine.shader.Shader;
import org.skycastle.flowgine.shader.ShaderRef;
import org.skycastle.flowgine.texture.Texture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SpatialShape extends AbstractSpatial {

    //private Map<String, Object> shapeParameters = new HashMap<String, Object>();
    private Map<String, Object> shaderParameters = new HashMap<String, Object>();

    private String shapeRef; // Contains shape params
    private ShaderRef shaderRef;
    private List<String> textureRefs;  // Contains texture params

    private Shape shape;
    private Shader shader;
    private List<Texture> textures;


    @Override protected void doCreate() {

        // TODO: Load (and possibly generate) shape - pass parameters to shape manager in shapeRef
        // Parameters could be in the shapeRef.  Parameters could be applied later as well and the shape re-generated, although it will
        // affect all users of the same shapeRef.  Maybe have a getUniqueShape method.
        // One use case is many identical trees or similar, can use same vertex object.  On the other hand, many objects are unique.

        // TODO: Load texture(s).  Possibly generate.  Parameters in textureRef.
        // Same applies as to shape manager above (runtime regenerate and re-uploading of texture possible

        // Load the shader (shaders are not generated, so it does not need to support any creation parameters).
        shader = FlowGine.shaderManager.get(shaderRef);

    }

    @Override protected void doUpdate(double deltaSeconds, double gameTimeSeconds) {

        // TODO: Update the shape if it is animated

        // TODO: Update the texture if it is animated

    }

    @Override protected void doRender() {

        // TODO: Do any projection

        // TODO: Do any instance specific scaling, rotation (and possibly mirroring), to make it easy to reuse the same vertex obj instance (e.g. tree, stone)

        shader.begin();
        // Apply shader parameters (TODO: Somehow unapply ones that are not specified?)
        shader.setUniforms(shaderParameters);

        // TODO: Bind textures

        // TODO: Bind VBO and index buffer

        // TODO: Render triangles

        shader.end();
    }

    @Override protected void doDelete() {
        // TODO: Release shape
        // TODO: Release textures

        // Release shader using shader pool
        FlowGine.shaderManager.release(shader);

    }
}
