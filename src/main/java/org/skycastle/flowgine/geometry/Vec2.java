package org.skycastle.flowgine.geometry;

import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.Vector2f;

/**
 *
 */
public class Vec2 extends Vector2f {

    public Vec2() {
    }

    public Vec2(ReadableVector2f src) {
        super(src);
    }

    public Vec2(float x, float y) {
        super(x, y);
    }
}
