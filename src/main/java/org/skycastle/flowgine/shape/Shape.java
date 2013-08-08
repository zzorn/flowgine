package org.skycastle.flowgine.shape;

import org.flowutils.Check;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.skycastle.flowgine.geometry.Col4;
import org.skycastle.flowgine.geometry.Vec2;
import org.skycastle.flowgine.geometry.Vec3;
import org.skycastle.flowgine.utils.Disposable;
import org.skycastle.flowgine.utils.OpenGLUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
// TODO: Add vertex buffer handles to this class
public class Shape implements Disposable {

    private List<Vec3> positions = new ArrayList<Vec3>();
    private List<Vec3> normals = new ArrayList<Vec3>();
    private List<Vec2> texturePositions = new ArrayList<Vec2>();
    private List<Col4> colors = new ArrayList<Col4>();

    private int vertexCount = 0;

    private List<Integer> indexes = new ArrayList<Integer>();



    private Map<String, Float> shaderParameters = new HashMap<String, Float>();


    /**
     * Adds a number of default vertexes.
     * @param vertexCountToAdd number of vertexes to add.
     * @return index of first added vertex.
     */
    public int addVertexes(int vertexCountToAdd) {
        int first = vertexCount;

        for (int i = 0; i < vertexCountToAdd; i++) {
            addVertex();
        }

        return first;
    }

    public int addVertex() {
        return addVertex(new Vec3(), new Vec2(), new Col4(), new Vec3(0,0,1));
    }

    public int addVertex(Vec3 pos) {
        return addVertex(pos, new Vec2(), new Col4(), new Vec3(0,0,1));
    }

    public int addVertex(Vec3 pos, Vec2 texPos, Col4 color) {
        return addVertex(pos, texPos, color, new Vec3(0,0,1));
    }

    public int addVertex(Vec3 pos, Vec2 texPos, Col4 color, Vec3 normal) {
        positions.add(pos);
        normals.add(normal);
        colors.add(color);
        texturePositions.add(texPos);

        vertexCount++;
        return vertexCount - 1;
    }

    public void addIndex(int vertexId) {
        indexes.add(vertexId);
    }

    public Vec3 pos(int vertex) {
        Check.under(vertex, "vertex", vertexCount);
        return positions.get(vertex);
    }

    public void setPos(int vertex, Vec3 pos) {
        Check.under(vertex, "vertex", vertexCount);
        positions.get(vertex).set(pos);
    }

    public void setPos(int vertex, float x, float y, float z) {
        Check.under(vertex, "vertex", vertexCount);
        positions.get(vertex).set(x, y, z);
    }

    public Vec3 normal(int vertex) {
        Check.under(vertex, "vertex", vertexCount);
        return normals.get(vertex);
    }

    public void setNormal(int vertex, Vec3 normal) {
        Check.under(vertex, "vertex", vertexCount);
        normals.get(vertex).set(normal);
    }

    public void setNormal(int vertex, float x, float y, float z) {
        Check.under(vertex, "vertex", vertexCount);
        normals.get(vertex).set(x, y, z);
    }

    public Vec2 texturePos(int vertex) {
        Check.under(vertex, "vertex", vertexCount);
        return texturePositions.get(vertex);
    }

    public void setTexturePos(int vertex, float u, float v) {
        Check.under(vertex, "vertex", vertexCount);
        texturePositions.get(vertex).set(u, v);
    }

    public void setTexturePos(int vertex, Vec2 tex) {
        Check.under(vertex, "vertex", vertexCount);
        texturePositions.get(vertex).set(tex);
    }

    public Col4 color(int vertex) {
        Check.under(vertex, "vertex", vertexCount);
        return colors.get(vertex);
    }

    public void setColor(int vertex, Col4 color) {
        Check.under(vertex, "vertex", vertexCount);
        colors.get(vertex).set(color);
    }

    /**
     * Color components in range 0..1 (or over, if the shader can manage that).
     * @param vertex index of vertex to change
     */
    public void setColor(int vertex, float r, float g, float b, float a) {
        Check.under(vertex, "vertex", vertexCount);
        colors.get(vertex).set(r, g, b, a);
    }

    public void addTriangle(int vertex1, int vertex2, int vertex3) {
        indexes.add(vertex1);
        indexes.add(vertex2);
        indexes.add(vertex3);
    }

    public void addQuad(int vertex1, int vertex2, int vertex3, int vertex4) {
        addTriangle(vertex1, vertex2, vertex3);
        addTriangle(vertex3, vertex4, vertex1);
    }

    public void recalculateNormals() {
        // TODO: Loop all triangles, calculate normals
    }

    public void begin() {
        // Bind vertex array and buffer objects
        // TODO
    }

    public void render() {
        begin();

        // Render triangles
        // TODO

        end();
    }

    public void end() {
        // Unbind vertex array and buffer objects
        // TODO
    }


    public int createVertexBufferObject() {

        int posElements = 3;
        int normalElements = 3;
        int texElements = 2;
        int colorElements = 4;

        int posByteOffset = 0;
        int normalByteOffset = posByteOffset + posElements * 4;
        int texByteOffset = normalByteOffset + normalElements * 4;
        int colorByteOffset = texByteOffset + texElements  * 4;

        int vertexDataSize = posElements  + normalElements  + texElements  + colorElements;
        int vertexDataByteSize = vertexDataSize * 4;

        // Prepare vertex buffer

        // Put each 'Vertex' in one FloatBuffer
        FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexCount * vertexDataSize);
        for (int i = 0; i < vertexCount; i++) {
            // Add vertex data to buffer
            positions.get(i).store(vertexDataBuffer);
            normals.get(i).store(vertexDataBuffer);
            texturePositions.get(i).store(vertexDataBuffer);
            colors.get(i).store(vertexDataBuffer);
        }
        vertexDataBuffer.flip();


        // Prepare index buffer

        final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indexes.size());
        for (Integer index : indexes) {
            indicesBuffer.put(index);
        }
        indicesBuffer.flip();


        // Create a new Vertex Array Object in memory and select it (bind)
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create a new Vertex Buffer Object in memory and select it (bind)
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexDataBuffer, GL15.GL_STREAM_DRAW);

        // Setup interleaved data buffers
        GL20.glVertexAttribPointer(0, posElements,    GL11.GL_FLOAT, false, vertexDataByteSize, posByteOffset);
        GL20.glVertexAttribPointer(1, normalElements, GL11.GL_FLOAT, false, vertexDataByteSize, normalByteOffset);
        GL20.glVertexAttribPointer(2, texElements,    GL11.GL_FLOAT, false, vertexDataByteSize, texByteOffset);
        GL20.glVertexAttribPointer(3, colorElements,  GL11.GL_FLOAT, false, vertexDataByteSize, colorByteOffset);

        // Deselect vertex buffer object
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Deselect the VAO
        GL30.glBindVertexArray(0);

        // Create a new VBO for the indices and select it (bind) - INDICES
        int vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);


        // Check for any errors
        OpenGLUtils.checkGLError("Creating shape");

        return vaoId;
    }

    /**
     * Free any resources used by the shape.
     */
    public void dispose() {
        // TODO
    }
}
