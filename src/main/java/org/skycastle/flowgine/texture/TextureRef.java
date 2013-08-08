package org.skycastle.flowgine.texture;

import org.flowutils.Check;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A reference to a texture resource.
 * In case the resource is a procedural texture, creation time parameters can be passed to it.
 */
public final class TextureRef {

    private final String path;
    private final Map<String, Object> parameters;

    /**
     * @param path path to texture resource.
     */
    public TextureRef(String path) {
        Check.nonEmptyString(path, "path");

        this.path = path;
        parameters = Collections.emptyMap();
    }

    /**
     * @param path path to texture resource.
     * @param parameters parameters to be used when generating the texture from the resource.
     *                   Mostly useful when the resource is a procedural texture.
     */
    public TextureRef(String path, Map<String, Object> parameters) {
        Check.nonEmptyString(path, "path");
        Check.notNull(parameters, "parameters");

        this.path = path;
        this.parameters = new HashMap<String, Object>(parameters);
    }

    /**
     * @return path to the texture resource.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return parameters to use when generating the texture.
     */
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextureRef that = (TextureRef) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
