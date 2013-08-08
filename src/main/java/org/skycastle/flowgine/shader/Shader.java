package org.skycastle.flowgine.shader;

import org.flowutils.Check;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.skycastle.flowgine.geometry.Col4;
import org.skycastle.flowgine.geometry.Vec2;
import org.skycastle.flowgine.geometry.Vec3;
import org.skycastle.flowgine.utils.FileUtils;
import org.skycastle.flowgine.utils.OpenGLUtils;

import java.io.File;
import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;


/**
 * A shader program, consisting of a vertex and fragment shader.
 * <p/>
 * Can have various uniform parameters set.
 * <p/>
 * Use begin and end when modifying the parameters, or applying the shader to geometry.
 * <p/>
 * To avoid compiling the same shader multiple times for different objects, use ShaderManager.
 */
public class Shader {

    private static final int MAX_LOG_LENGTH = 1024 * 8;
    private static final int NAME_BUFFER_SIZE = 1024;
    private static final int INVALID_HANDLE = 0;

    private List<String> uniformNames = new ArrayList<String>();
    private final Map<String, Integer> uniformLocations = new HashMap<String, Integer>();

    private int shaderProgramHandle;
    private int vertexShaderHandle;
    private int fragmentShaderHandle;

    private String vertexShaderName;
    private String fragmentShaderName;
    private final String vertexShaderSource;
    private final String fragmentShaderSource;

    private String errorMessage = null;

    private final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);


    /**
     * Creates and compiles a shader program from the specified vertex and fragment shaders files.
     *
     * @param vertexShader   file with vertex shader source.
     * @param fragmentShader file with fragment shader source.
     */
    public Shader(File vertexShader, File fragmentShader) {
        this(vertexShader.getPath(), FileUtils.readFile(vertexShader),
             fragmentShader.getPath(), FileUtils.readFile(fragmentShader));
    }

    /**
     * Creates and compiles a shader program from the specified vertex and fragment shaders resources.
     * Uses classloader to load the resources from the jar the program is in.
     *
     * @param vertexShaderResourcePath   resource path for the vertex shader sources.
     * @param fragmentShaderResourcePath resource path for the fragment shader sources.
     */
    public Shader(String vertexShaderResourcePath, String fragmentShaderResourcePath) {
        this(vertexShaderResourcePath, FileUtils.readResource(vertexShaderResourcePath),
             fragmentShaderResourcePath, FileUtils.readResource(fragmentShaderResourcePath));
    }

    /**
     * Creates and compiles a shader program from the specified vertex and fragment shaders sources.
     *
     * @param vertexShaderName     name of vertex shader for debugging purposes, e.g. filename.
     * @param vertexShaderSource   vertex shader source.
     * @param fragmentShaderName   name of fragment shader for debugging purposes, e.g. filename.
     * @param fragmentShaderSource fragment shader source.
     */
    public Shader(String vertexShaderName, String vertexShaderSource,
                  String fragmentShaderName, String fragmentShaderSource) {
        Check.nonEmptyString(vertexShaderName, "vertexShaderName");
        Check.nonEmptyString(vertexShaderSource, "vertexShaderSource");
        Check.nonEmptyString(fragmentShaderName, "fragmentShaderName");
        Check.nonEmptyString(fragmentShaderSource, "fragmentShaderSource");

        this.vertexShaderName = vertexShaderName;
        this.fragmentShaderName = fragmentShaderName;
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;

        // Compile shader
        errorMessage = compile();

        // TODO: Log with some framework
        if (errorMessage != null) {
            System.err.println(errorMessage);
        }

        // Check for other opengl errors
        OpenGLUtils.checkGLError("creating shader with vertex shader '" +
                                 vertexShaderName +
                                 "' and fragment shader '" +
                                 fragmentShaderName +
                                 "'");
    }

    /**
     * Binds shader.
     * Must be called before attributes or uniforms are set, and before rendering with the shader.
     */
    public void begin() {
        glUseProgram(shaderProgramHandle);
    }

    /**
     * Unbinds shader.
     */
    public void end() {
        glUseProgram(0);
    }

    /**
     * @return true if there were some errors when compiling or creating the shaders.
     */
    public boolean hasErrors() {
        return errorMessage != null;
    }

    /**
     * @return Error message describing compilation or other problems, or null if there was no errors.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return list of available uniform names in the shader.
     */
    public List<String> getUniformNames() {
        return uniformNames;
    }


    /**
     * Sets uniform float value.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformFloat(String name, float f) {
        glUniform1f(getUniformLocation(name), f);
    }

    /**
     * Sets uniform float vector 2.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformFloat2(String name, float f1, float f2) {
        glUniform2f(getUniformLocation(name), f1, f2);
    }

    /**
     * Sets uniform float vector 3.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformFloat3(String name, float f1, float f2, float f3) {
        glUniform3f(getUniformLocation(name), f1, f2, f3);
    }

    /**
     * Sets uniform float vector 4.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformFloat4(String name, float f1, float f2, float f3, float f4) {
        glUniform4f(getUniformLocation(name), f1, f2, f3, f4);
    }

    /**
     * Set uniform integer value.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformInt(String name, int i) {
        glUniform1i(getUniformLocation(name), i);
    }

    /**
     * Sets uniform integer vector 2.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformInt2(String name, int i1, int i2) {
        glUniform2i(getUniformLocation(name), i1, i2);
    }

    /**
     * Sets uniform integer vector 3.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformInt3(String name, int i1, int i2, int i3) {
        glUniform3i(getUniformLocation(name), i1, i2, i3);
    }

    /**
     * Sets uniform integer vector 4.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformInt4(String name, int i1, int i2, int i3, int i4) {
        glUniform4i(getUniformLocation(name), i1, i2, i3, i4);
    }

    /**
     * Sets uniform float vector 2.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformVec2(String name, Vec2 vec) {
        setUniformFloat2(name, vec.x, vec.y);
    }

    /**
     * Sets uniform float vector 3.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformVec3(String name, Vec3 vec) {
        setUniformFloat3(name, vec.x, vec.y, vec.z);
    }

    /**
     * Sets uniform color.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformColor(String name, Col4 color) {
        setUniformFloat4(name, color.r, color.g, color.b, color.a);
    }

    /**
     * Sets uniform matrix 3.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformMatrix3(String name, Matrix3f matrix) {
        int location = getUniformLocation(name);
        matrixBuffer.clear();
        matrix.store(matrixBuffer);
        glUniformMatrix3(location, false, matrixBuffer);
    }

    /**
     * Sets uniform matrix 4.
     * Should be called while the shader is bound (after begin and before end).
     */
    public void setUniformMatrix4(String name, Matrix4f matrix) {
        int location = getUniformLocation(name);
        matrixBuffer.clear();
        matrix.store(matrixBuffer);
        glUniformMatrix4(location, false, matrixBuffer);
    }

    /**
     * Applies the provided parameters to this shader.
     * @param shaderParameters parameters to set.  Supported types are Integer, Float, Vec2, Vec3, Col4, and matrixes.
     * @throws IllegalArgumentException if the types are unsupported or no parameter with a specified name is found.
     */
    public void setUniforms(Map<String, Object> shaderParameters) {
        for (Map.Entry<String, Object> entry : shaderParameters.entrySet()) {
            final String name = entry.getKey();
            final Object value = entry.getValue();

            Check.identifier(name, "parameter name");
            Check.notNull(value, "value for parameter '"+name+"'");

            if (Integer.class.isInstance(value)) {
                setUniformInt(name, (Integer) value);
            }
            else if (Float.class.isInstance(value)) {
                setUniformFloat(name, (Float) value);
            }
            else if (Vec3.class.isInstance(value)) {
                setUniformVec3(name, (Vec3) value);
            }
            else if (Vec2.class.isInstance(value)) {
                setUniformVec2(name, (Vec2) value);
            }
            else if (Matrix4f.class.isInstance(value)) {
                setUniformMatrix4(name, (Matrix4f) value);
            }
            else if (Matrix3f.class.isInstance(value)) {
                setUniformMatrix3(name, (Matrix3f) value);
            }
            else {
                throw new IllegalArgumentException("Unsupported type '"+value.getClass()+"' for parameter '"+name+"'");
            }
        }
    }


    /**
     * @return true if a parameter with the specified name exists in this shader.
     */
    public boolean hasUniform(String name) {
        return uniformLocations.containsKey(name);
    }

    /**
     * Free any resources used by this shader program.
     */
    public void delete() {
        // Unbind shader
        glUseProgram(0);

        // Delete
        glDeleteShader(vertexShaderHandle);
        glDeleteShader(fragmentShaderHandle);
        glDeleteProgram(shaderProgramHandle);
    }

    /**
     * @return name of the vertex shader (e.g. filename).
     */
    public String getVertexShaderName() {
        return vertexShaderName;
    }

    /**
     * @return name of the fragment shader (e.g. filename).
     */
    public String getFragmentShaderName() {
        return fragmentShaderName;
    }

    /**
     * @return the vertex shader source.
     */
    public String getVertexShaderSource() {
        return vertexShaderSource;
    }

    /**
     * @return the fragment shader source.
     */
    public String getFragmentShaderSource() {
        return fragmentShaderSource;
    }

    /**
     * @return location of the specified parameter uniform.
     */
    private int getUniformLocation(String name) {
        Integer location = uniformLocations.get(name);
        if (location == null) throw new IllegalArgumentException("No uniform named '" + name + "' found");

        return location;
    }

    /**
     * Compiles vertex and fragment shaders, links them to shader program.
     *
     * @return null if all ok, otherwise error message.
     */
    private String compile() {
        StringBuilder errorMessages = new StringBuilder();

        // Compile vertex shader
        vertexShaderHandle = compileShader(vertexShaderName, GL_VERTEX_SHADER, vertexShaderSource, errorMessages);
        if (vertexShaderHandle == INVALID_HANDLE) {
            return errorMessages.toString();
        }

        // Compile fragment shader
        fragmentShaderHandle = compileShader(fragmentShaderName, GL_FRAGMENT_SHADER, fragmentShaderSource, errorMessages);
        if (fragmentShaderHandle == INVALID_HANDLE) {
            return errorMessages.toString();
        }

        // Link shaders to final program
        shaderProgramHandle = compileProgram(errorMessages);
        if (shaderProgramHandle == INVALID_HANDLE) {
            return errorMessages.toString();
        }

        // Create lookup table for shader parameters
        readUniformLocations();

        return null;
    }

    /**
     * @param name          name of the shader to load
     * @param type          type of shader
     * @param source        source code for the shader
     * @param errorMessages string buffer to save any error messages in.
     * @return handle of the loaded shader, or INVALID_HANDLE if there was an error.
     */
    private int compileShader(String name, int type, String source, StringBuilder errorMessages) {

        // Create handle
        int shaderHandle = glCreateShader(type);
        if (shaderHandle == INVALID_HANDLE) {
            errorMessages.append("Error when creating a shader handle for " + name + ":\n");
            errorMessages.append(glGetShaderInfoLog(shaderHandle, MAX_LOG_LENGTH));
            return INVALID_HANDLE;
        }

        // Compile shader
        glShaderSource(shaderHandle, source);
        glCompileShader(shaderHandle);
        boolean compiled = getCompileStatus(shaderHandle);
        if (!compiled) {
            errorMessages.append("Error when compiling " + name + ":\n");
            errorMessages.append(glGetShaderInfoLog(shaderHandle, MAX_LOG_LENGTH));
            return INVALID_HANDLE;
        }

        return shaderHandle;
    }

    /**
     * @param errorMessages string buffer to save any error messages in.
     * @return handle for the shader program, or INVALID_HANDLE if there was an error.
     */
    private int compileProgram(StringBuilder errorMessages) {
        // Create handle for the program
        int programHandle = glCreateProgram();
        if (programHandle == INVALID_HANDLE) {
            errorMessages.append("Could not allocate shader program handle for " +
                                 vertexShaderName +
                                 " and " +
                                 fragmentShaderName +
                                 ":\n");
            errorMessages.append(glGetProgramInfoLog(programHandle, MAX_LOG_LENGTH));
            return INVALID_HANDLE;
        }

        // Link sub-shaders together
        glAttachShader(programHandle, vertexShaderHandle);
        glAttachShader(programHandle, fragmentShaderHandle);
        glLinkProgram(programHandle);
        boolean linked = getLinkStatus(programHandle);
        if (!linked) {
            errorMessages.append("Error when linking " + vertexShaderName + " and " + fragmentShaderName + " :\n");
            errorMessages.append(glGetProgramInfoLog(programHandle, MAX_LOG_LENGTH));
            return INVALID_HANDLE;
        }

        return programHandle;
    }

    private boolean getCompileStatus(int shaderHandle) {
        IntBuffer compileStatusBuffer = BufferUtils.createIntBuffer(1);
        glGetShader(shaderHandle, GL_COMPILE_STATUS, compileStatusBuffer);
        return compileStatusBuffer.get(0) == GL_TRUE;
    }

    private boolean getLinkStatus(int shaderHandle) {
        IntBuffer linkStatusBuffer = BufferUtils.createIntBuffer(1);
        glGetProgram(shaderHandle, GL_LINK_STATUS, linkStatusBuffer);
        return linkStatusBuffer.get(0) == GL_TRUE;
    }

    private void readUniformLocations() {
        // Get number of uniforms in the program
        intBuffer.clear();
        glGetProgram(shaderProgramHandle, GL_ACTIVE_UNIFORMS, intBuffer);
        int uniformCount = intBuffer.get(0);

        // Get names and locations of the uniforms in the program
        uniformNames.clear();
        uniformLocations.clear();
        for (int i = 0; i < uniformCount; i++) {
            String name = glGetActiveUniform(shaderProgramHandle, i, NAME_BUFFER_SIZE);
            int location = glGetUniformLocation(shaderProgramHandle, name);
            uniformNames.add(name);
            uniformLocations.put(name, location);
        }
    }

}
