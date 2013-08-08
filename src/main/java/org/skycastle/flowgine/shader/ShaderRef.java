package org.skycastle.flowgine.shader;

/**
 * Specifies a vertex and a fragment shader to use when creating a shader.
 */
public final class ShaderRef {
    private final String vertexShaderPath;
    private final String fragmentShaderPath;

    public ShaderRef(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShaderPath = vertexShaderPath;
        this.fragmentShaderPath = fragmentShaderPath;
    }

    public String getVertexShaderPath() {
        return vertexShaderPath;
    }

    public String getFragmentShaderPath() {
        return fragmentShaderPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShaderRef that = (ShaderRef) o;

        if (fragmentShaderPath != null ?
            !fragmentShaderPath.equals(that.fragmentShaderPath) :
            that.fragmentShaderPath != null) return false;
        if (vertexShaderPath != null ? !vertexShaderPath.equals(that.vertexShaderPath) : that.vertexShaderPath != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertexShaderPath != null ? vertexShaderPath.hashCode() : 0;
        result = 31 * result + (fragmentShaderPath != null ? fragmentShaderPath.hashCode() : 0);
        return result;
    }
}
