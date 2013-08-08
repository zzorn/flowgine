package org.skycastle.flowgine.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.skycastle.flowgine.geometry.Col4;
import org.skycastle.flowgine.geometry.Vec3;
import org.skycastle.flowgine.geometry.Frame;
import org.skycastle.flowgine.shape.Shape;

/**
 * Utilities for working with 3D geometry.
 */
public class GeomUtils {

    /**
     * Creates a perspective projection matrix, for a screen with the specified aspect ratio.
     *
     * @param aspect aspect.  Use width / height.  1.0 = square display, 2.0 = twice as wide as high, and so on.
     * @param near near clip plane distance
     * @param far far clip plane distance
     * @param out the matrix to store the perspective projection matrix in, or null to create a new one.
     * @return the projection matrix
     */
    public static Matrix4f perspectiveProjection(float aspect, float near,float far, Matrix4f out) {
        return perspectiveProjection(-1f, 1f, -1f/aspect, 1f/aspect, near, far, out);
    }

    /**
     * Creates a perspective projection matrix.
     * @param left left edge
     * @param right right edge
     * @param bottom bottom edge
     * @param top top edge
     * @param near near clip plane distance
     * @param far far clip plane distance
     * @param out the matrix to store the perspective projection matrix in, or null to create a new one.
     * @return the projection matrix
     */
    public static Matrix4f perspectiveProjection(float left,float right,float bottom,float top,float near,float far, Matrix4f out) {
        final Matrix4f matrix;
        if (out != null) matrix = out;
        else matrix = new Matrix4f();

        matrix.setIdentity();

        matrix.m00 = 2*near/(right - left);
        matrix.m11 = 2*near/(top - bottom);
        matrix.m22 = -(far +near)/(far - near);
        matrix.m23 = -1;
        matrix.m32 = -2*far*near/(far - near);
        matrix.m20 = (right+left)/(right -left);
        matrix.m21 = (top + bottom)/(top-bottom);
        matrix.m33 = 0;

        return matrix;
    }


    public static Shape makeCube(Vec3 center, Vec3 size, Col4 color, Shape out) {
        if (out == null) out = new Shape();

        // Create base of cube
        Frame base = new Frame(center, size.x, size.z);

        // Extrude cube
        final Frame sideU1 = new Frame();
        final Frame sideU2 = new Frame();
        final Frame sideV1 = new Frame();
        final Frame sideV2 = new Frame();
        final Frame top = base.extrude(new Vec3(0, size.y, 0),
                                        1,
                                        1,
                                        sideU1,
                                        sideU2,
                                        sideV1,
                                        sideV2);
        // Make base point down, to get face facing outward.
        base.invert();

        // Triangulate cube
        base.fillRect(out, color);
        sideU1.fillRect(out, color);
        sideU2.fillRect(out, color);
        sideV1.fillRect(out, color);
        sideV2.fillRect(out, color);
        top.fillRect(out, color);

        return out;
    }

}
