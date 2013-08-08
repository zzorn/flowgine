package org.skycastle.flowgine.shape;

import org.flowutils.Check;
import org.skycastle.flowgine.texture.TextureRef;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A reference to a 3D shape resource.
 * In case the shape is a procedural shape, creation time parameters can be passed to it.
 */
public final class ShapeRef {

    private final String path;
    private final Map<String, Object> parameters;

    /**
     * @param path path to shape resource.
     */
    public ShapeRef(String path) {
        Check.nonEmptyString(path, "path");

        this.path = path;
        parameters = Collections.emptyMap();
    }

    /**
     * @param path path to shape resource.
     * @param parameters parameters to be used when generating the shape from the resource, e.g. if the shape is procedural.
     */
    public ShapeRef(String path, Map<String, Object> parameters) {
        Check.nonEmptyString(path, "path");
        Check.notNull(parameters, "parameters");

        this.path = path;
        this.parameters = new HashMap<String, Object>(parameters);
    }

    /**
     * @return path to the shape resource.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return parameters to use when generating the shape.
     */
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShapeRef shapeRef = (ShapeRef) o;

        if (parameters != null ? !parameters.equals(shapeRef.parameters) : shapeRef.parameters != null) return false;
        if (path != null ? !path.equals(shapeRef.path) : shapeRef.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
