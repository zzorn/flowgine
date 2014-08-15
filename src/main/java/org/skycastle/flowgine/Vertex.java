package org.skycastle.flowgine;

import org.skycastle.flowgine.geometry.Col4;
import org.skycastle.flowgine.geometry.Vec2;
import org.skycastle.flowgine.geometry.Vec3;

/**
 * Contains vertex data.
 */
public final class Vertex {
    public final Vec3 pos    = new Vec3();
    public final Vec3 normal = new Vec3(0,0,1);
    public final Vec2 tex    = new Vec2();
    public final Col4 color  = new Col4(1, 1, 1);

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Vec2 getTex() {
        return tex;
    }

    public Col4 getColor() {
        return color;
    }
}
