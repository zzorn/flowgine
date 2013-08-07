package org.skycastle.flowgine.utils;

import org.skycastle.flowgine.utils.Shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of loaded shaders, and ensures there is only one of each loaded at a time.
 * Frees unused shaders from the graphics card.
 */
public class ShaderManager {

    private static final Map<String, Shader> shaders = new HashMap<String, Shader>();
    private static final Map<Shader, String> shaderKeys = new HashMap<Shader, String>();
    private static final Map<String, Integer> shaderUserCount = new HashMap<String, Integer>();

    /**
     * Call this to get a specific shader.
     * Will load the shader if it is not already used by some other code.
     * @param vertexShaderFile file with vertex shader source.
     * @param fragmentShaderFile  file with fragment shader source.
     * @return the shader.
     */
    public static Shader useShader(File vertexShaderFile, File fragmentShaderFile) {
        // Get key for shader based on shader file names
        String key = getKey(vertexShaderFile.getPath(), fragmentShaderFile.getPath());

        Shader shader = shaders.get(key);
        if (shader == null) {
            // Create shader if needed
            shader = new Shader(vertexShaderFile, fragmentShaderFile);
            addShader(key, shader);
        }

        // One more user now
        increaseUsers(key);

        return shader;
    }

    /**
     * Call this to get a specific shader.
     * Will load the shader if it is not already used by some other code.
     *
     * @param vertexShaderResource resource on classpath with vertex shader source.
     * @param fragmentShaderResource resource on classpath with fragment shader source.
     * @return the shader.
     */
    public static Shader useShader(String vertexShaderResource, String fragmentShaderResource) {
        // Get key for shader based on shader resource names
        String key = getKey(vertexShaderResource, fragmentShaderResource);

        Shader shader = shaders.get(key);
        if (shader == null) {
            // Create shader if needed
            shader = new Shader(vertexShaderResource, fragmentShaderResource);
            addShader(key, shader);
        }

        // One more user now
        increaseUsers(key);

        return shader;
    }

    /**
     * Call this when a shader is no longer needed by code that earlier called useShader.
     * If the shader has no other users, it will be disposed.
     */
    public static void releaseShader(Shader shader) {
        decreaseUsers(shader);
    }

    /**
     * Delete all loaded shaders, can be called when exiting a program.
     */
    public static void deleteAll() {
        for (Shader shader : shaders.values()) {
            shader.delete();
        }
        shaders.clear();
        shaderKeys.clear();
        shaderUserCount.clear();
    }

    private static void addShader(String key, Shader shader) {
        shaders.put(key, shader);
        shaderKeys.put(shader, key);
    }

    private static void increaseUsers(String key) {
        // Get current user count
        Integer userCount = shaderUserCount.get(key);
        if (userCount == null) userCount = 0;

        // Increase users
        shaderUserCount.put(key, userCount + 1);
    }

    private static void decreaseUsers(Shader shader) {
        final String key = shaderKeys.get(shader);

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

    private static String getKey(String vertexShaderName, String fragmentShaderName) {
        return vertexShaderName + "::" + fragmentShaderName;
    }

}
