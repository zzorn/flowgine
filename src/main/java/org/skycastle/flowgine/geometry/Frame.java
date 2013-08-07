package org.skycastle.flowgine.geometry;

import org.skycastle.flowgine.Col4;
import org.skycastle.flowgine.Shape;
import org.skycastle.flowgine.Vec2;
import org.skycastle.flowgine.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Square frame that can be extruded and manipulated.
 */
public final class Frame {

    public Vec3 u1v1 = new Vec3( 0.5f, 0, -0.5f);
    public Vec3 u2v1 = new Vec3(-0.5f, 0, -0.5f);
    public Vec3 u1v2 = new Vec3( 0.5f, 0,  0.5f);
    public Vec3 u2v2 = new Vec3(-0.5f, 0,  0.5f);

    private static Vec3 tempU  = new Vec3();
    private static Vec3 tempV  = new Vec3();
    private static Vec3 tempU1 = new Vec3();
    private static Vec3 tempU2 = new Vec3();

    public Frame() {
    }

    public Frame(float w, float d) {
        this(null, w, d);
    }

    public Frame(Vec3 u1v1in,
                 Vec3 u2v1in,
                 Vec3 u1v2in,
                 Vec3 u2v2in) {
        u1v1.set(u1v1in);
        u2v1.set(u2v1in);
        u1v2.set(u1v2in);
        u2v2.set(u2v2in);
    }

    public Frame(final Vec3 center, float w, float d) {
        u1v1.scale(w, 0, d);
        u2v1.scale(w, 0, d);
        u1v2.scale(w, 0, d);
        u2v2.scale(w, 0, d);

        if (center != null) {
            u1v1.add(center);
            u2v1.add(center);
            u1v2.add(center);
            u2v2.add(center);
        }
    }

    public Vec3 getCenter(Vec3 target) {
        return interpolateCorners(0.5f, 0.5f, target);
    }

    public Vec3 getForward(Vec3 target) {
        if (target == null) target = new Vec3();

        tempU.setSub(u2v1, u1v1);
        tempV.setSub(u1v2, u1v1);
        target.setCross(tempU, tempV).normalize();

        return target;
    }

    /**
     * Flip sides of face.
     */
    public void invert() {
        // Swap two diagonal corners
        u2v1.swap(u1v2);
    }

    public void set(Frame frame) {
        u1v1.set(frame.u1v1);
        u2v1.set(frame.u2v1);
        u1v2.set(frame.u1v2);
        u2v2.set(frame.u2v2);
    }

    public void translate(Vec3 offs) {
        u1v1.add(offs);
        u2v1.add(offs);
        u1v2.add(offs);
        u2v2.add(offs);
    }

    /**
     * Extrudes the frame.
     * @param dir vector to move center for extruded end.
     * @param scaleU
     * @param scaleV
     * @param sideU1
     * @param sideU2
     * @param sideV1
     * @param sideV2
     * @return extruded end frame
     */
    public Frame extrude(Vec3 dir,
                         float scaleU,
                         float scaleV,
                         Frame sideU1,
                         Frame sideU2,
                         Frame sideV1,
                         Frame sideV2) {

        Frame end = new Frame();

        end.set(this);
        end.translate(dir);
        end.rescale(scaleU, scaleV);

        if (sideV1 != null) {
            sideV1.u1v1.set(u2v1);
            sideV1.u1v2.set(u1v1);
            sideV1.u2v1.set(end.u2v1);
            sideV1.u2v2.set(end.u1v1);
        }

        if (sideV2 != null) {
            sideV2.u1v2.set(u2v2);
            sideV2.u1v1.set(u1v2);
            sideV2.u2v2.set(end.u2v2);
            sideV2.u2v1.set(end.u1v2);
        }

        if (sideU1 != null) {
            sideU1.u1v1.set(u1v1);
            sideU1.u1v2.set(u1v2);
            sideU1.u2v1.set(end.u1v1);
            sideU1.u2v2.set(end.u1v2);
        }

        if (sideU2 != null) {
            sideU2.u2v1.set(u2v1);
            sideU2.u2v2.set(u2v2);
            sideU2.u1v1.set(end.u2v1);
            sideU2.u1v2.set(end.u2v2);
        }

        return end;
    }

    private void rescale(final float scaleU, final float scaleV) {
        tempU.setSub(u2v1, u1v1).scale((scaleU - 1.0f) * 0.5f);
        u1v1.sub(tempU);
        u2v1.add(tempU);

        tempU.setSub(u2v2, u1v2).scale((scaleU - 1.0f) * 0.5f);
        u1v2.sub(tempU);
        u2v2.add(tempU);

        tempV.setSub(u1v2, u1v1).scale((scaleV - 1.0f) * 0.5f);
        u1v1.sub(tempV);
        u1v2.add(tempV);

        tempV.setSub(u2v2, u2v1).scale((scaleV - 1.0f) * 0.5f);
        u2v1.sub(tempV);
        u2v2.add(tempV);
    }

    public void fillRect(Shape shape, Col4 color) {
        final int i1 = shape.addVertex(u1v1, new Vec2(), color);
        final int i2 = shape.addVertex(u2v1, new Vec2(), color);
        final int i3 = shape.addVertex(u1v2, new Vec2(), color);
        final int i4 = shape.addVertex(u2v2, new Vec2(), color);
        shape.addQuad(i1, i2, i3, i4);
    }

    public float minSide() {
        float v1len = tempV.setSub(u2v1, u1v1).length();
        float v2len = tempV.setSub(u2v2, u1v2).length();
        float u1len = tempU.setSub(u1v2, u1v1).length();
        float u2len = tempU.setSub(u2v2, u2v1).length();

        return Math.min(
                Math.min(v1len, v2len),
                Math.min(u1len, u2len));
    }

    public float averageULen() {
        float u1len = tempV.set(u2v1).sub(u1v1).length();
        float u2len = tempV.set(u2v2).sub(u1v2).length();

        return 0.5f * (u1len + u2len);
    }

    public float averageVLen() {
        float v1len = tempU.set(u1v2).sub(u1v1).length();
        float v2len = tempU.set(u2v2).sub(u2v1).length();

        return 0.5f * (v1len + v2len);
    }

    public List<Frame> subdivide(int uCount, int vCount) {
        if (vCount < 1) vCount = 1;
        if (uCount < 1) uCount = 1;

        final List<Frame> frames = new ArrayList<Frame>();

        for (int vi = 0; vi < vCount; vi++) {
            for (int ui = 0; ui < uCount; ui++) {
                float u1w = (float)ui / uCount;
                float u2w = ((float)ui + 1) / uCount;
                float v1w = (float)vi / vCount;
                float v2w = ((float)vi + 1) / vCount;

                frames.add(subFrame(u1w, v1w, u2w, v2w));
            }
        }

        return frames;
    }

    public Frame subFrame(float relativeU1, float relativeV1, float relativeU2, float relativeV2) {
        Frame frame = new Frame();

        interpolateCorners(relativeU1, relativeV1, frame.u1v1);
        interpolateCorners(relativeU2, relativeV1, frame.u2v1);
        interpolateCorners(relativeU1, relativeV2, frame.u1v2);
        interpolateCorners(relativeU2, relativeV2, frame.u2v2);

        return frame;
    }

    public Vec3 interpolateCorners(float uPos, float vPos, Vec3 out) {
        if (out == null) out = new Vec3();

        tempU1.setScaled(1.0f - vPos, u1v1);
        tempU1.addScaled(vPos, u1v2);

        tempU2.setScaled(1.0f - vPos, u2v1);
        tempU2.addScaled(vPos, u2v2);

        out.setScaled(1.0f - uPos, tempU1);
        out.addScaled(uPos, tempU2);

        return out;
    }
}
