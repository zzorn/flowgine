package org.skycastle.flowgine.shader;

import org.skycastle.flowgine.FlowGine;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of loaded shaders, and ensures there is only one of each loaded at a time.
 * Frees unused shaders from the graphics card.
 */
public class ShaderManager {

    private final Map<ShaderRef, Shader> shaders = new HashMap<ShaderRef, Shader>();
    private final Map<Shader, ShaderRef> shaderKeys = new HashMap<Shader, ShaderRef>();
    private final Map<ShaderRef, Integer> shaderUserCount = new HashMap<ShaderRef, Integer>();

    /**
     * Call this to get a specific shader.
     * Will load the shader if it is not already used by some other code.
     * Uses FlowGine.resourceLoader to load the shaders.
     *
     * @param vertexShaderPath resource with vertex shader source.
     * @param fragmentShaderPath resource with fragment shader source.
     * @return the shader.
     */
    public Shader getShader(String vertexShaderPath, String fragmentShaderPath) {
        return getShader(new ShaderRef(vertexShaderPath, fragmentShaderPath));
    }

    /**
     * Call this to get a specific shader.
     * Will load the shader if it is not already used by some other code.
     * Uses FlowGine.resourceLoader to load the shaders.
     *
     * @param shaderRef configuration for the shader (vertex and fragment shader paths).
     * @return the shader
     */
    public Shader getShader(ShaderRef shaderRef) {
        Shader shader = shaders.get(shaderRef);
        if (shader == null) {
            // Create shader if needed
            final String vertexSource   = FlowGine.resourceLoader.loadResourceAsString(shaderRef.getVertexShaderPath());
            final String fragmentSource = FlowGine.resourceLoader.loadResourceAsString(shaderRef.getFragmentShaderPath());
            shader = new Shader(shaderRef.getVertexShaderPath(), vertexSource, shaderRef.getFragmentShaderPath(), fragmentSource);
            addShader(shaderRef, shader);
        }

        // One more user now
        increaseUsers(shaderRef);

        return shader;
    }

    /**
     * Call this when a shader is no longer needed by code that earlier called getShader.
     * If the shader has no other users, it will be disposed.
     */
    public void releaseShader(Shader shader) {
        decreaseUsers(shader);
    }

    /**
     * Delete all loaded shaders, can be called when exiting a program.
     */
    public void deleteAll() {
        for (Shader shader : shaders.values()) {
            shader.delete();
        }
        shaders.clear();
        shaderKeys.clear();
        shaderUserCount.clear();
    }

    private void addShader(ShaderRef key, Shader shader) {
        shaders.put(key, shader);
        shaderKeys.put(shader, key);
    }

    private void increaseUsers(ShaderRef key) {
        // Get current user count
        Integer userCount = shaderUserCount.get(key);
        if (userCount == null) userCount = 0;

        // Increase users
        shaderUserCount.put(key, userCount + 1);
    }

    private void decreaseUsers(Shader shader) {
        final ShaderRef key = shaderKeys.get(shader);

        // Get current user count
        Integer userCount = shaderUserCount.get(key);
        if (userCount == null) userCount = 0;

        // Decrease user count
        final int newUsers = userCount - 1;
        shaderUserCount.put(key, newUsers);

        // If no-one is using the shader, delete it
        if (newUsers <= 0) {
            shaders.remove(key);
            shaderKeys.remove(shader);
            shaderUserCount.remove(key);
            shader.delete();
        }
    }

    private String getKey(String vertexShaderName, String fragmentShaderName) {
        return vertexShaderName + "::" + fragmentShaderName;
    }

}
