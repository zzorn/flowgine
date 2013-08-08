package org.skycastle.flowgine.geometry;

import java.nio.FloatBuffer;

/**
 * Three dimensional float vector with basic operations.
 */
public class Vec3 {

    private static final long serialVersionUID = 1L;

    public float x = 0;
    public float y = 0;
    public float z = 0;

    public Vec3() {
    }

    public Vec3(Vec3 vec3) {
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public final Vec3 set(Vec3 src) {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    /**
     * Scale this vector with the specified scale.
     */
    public final Vec3 scale(float scale) {
        x *= scale;
        y *= scale;
        z *= scale;
        return this;
    }

    /**
     * Scale this vector with the specified scale along each axis.
     */
    public final Vec3 scale(float scaleX, float scaleY, float scaleZ) {
        x *= scaleX;
        y *= scaleY;
        z *= scaleZ;
        return this;
    }

    /**
     * Scale this vector with the specified scales for each axis.
     */
    public final Vec3 scale(Vec3 scale) {
        x *= scale.x;
        y *= scale.y;
        z *= scale.z;
        return this;
    }

    /**
     * Swaps this vector and the provided vector.
     * @return this vector.
     */
    public final Vec3 swap(Vec3 a) {
        float tx = x;
        float ty = y;
        float tz = z;

        x = a.x;
        y = a.y;
        z = a.z;

        a.x = tx;
        a.y = ty;
        a.z = tz;

        return this;
    }

    /**
     * Add a to this vector.
     */
    public final Vec3 add(Vec3 a) {
        x += a.x;
        y += a.y;
        z += a.z;

        return this;
    }

    /**
     * Add (x, y, z) to this vector.
     */
    public final Vec3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    /**
     * Subtract a from this vector.
     */
    public final Vec3 sub(Vec3 a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;

        return this;
    }

    /**
     * Add scaleA * a to this vector.
     */
    public final Vec3 addScaled(float scaleA, Vec3 a) {
        x += scaleA * a.x;
        y += scaleA * a.y;
        z += scaleA * a.z;

        return this;
    }

    /**
     * Add scaleA * a to this vector.
     */
    public final Vec3 addScaled(Vec3 scaleA, Vec3 a) {
        x += scaleA.x * a.x;
        y += scaleA.y * a.y;
        z += scaleA.z * a.z;

        return this;
    }

    /**
     * Add a to this vector, scaling each value of a with the specified scales.
     */
    public final Vec3 addScaled(float scaleX, float scaleY, float scaleZ, Vec3 a) {
        x += scaleX * a.x;
        y += scaleY * a.y;
        z += scaleZ * a.z;

        return this;
    }

    /**
     * Multiply this vector with scale and add scaleA * a to it.
     */
    public final Vec3 addScaled(float scale, float scaleA, Vec3 a) {
        x = scale * x + scaleA * a.x;
        y = scale * y + scaleA * a.y;
        z = scale * z + scaleA * a.z;

        return this;
    }

    /**
     * Set this vector to scaleA*a + scaleB*b.
     */
    public final Vec3 setAddScaled(float scaleA, Vec3 a, float scaleB, Vec3 b) {
        x = scaleA * a.x + scaleB * b.x;
        y = scaleA * a.y + scaleB * b.y;
        z = scaleA * a.z + scaleB * b.z;

        return this;
    }

    /**
     * Set this vector to a + b*scaleB.
     */
    public final Vec3 setAddScaled(Vec3 a, float scaleB, Vec3 b) {
        x = a.x + scaleB * b.x;
        y = a.y + scaleB * b.y;
        z = a.z + scaleB * b.z;

        return this;
    }

    /**
     * Set this vector to a * scaleA.
     */
    public final Vec3 setScaled(float scaleA, Vec3 a) {
        x = scaleA * a.x;
        y = scaleA * a.y;
        z = scaleA * a.z;

        return this;
    }

    /**
     * Set this vector to a + b.
     */
    public final Vec3 setAdd(Vec3 a, Vec3 b) {
        x = a.x + b.x;
        y = a.y + b.y;
        z = a.z + b.z;

        return this;
    }

    /**
     * Set this vector to a + (x, y, z).
     */
    public final Vec3 setAdd(Vec3 a, float x, float y, float z) {
        this.x = a.x + x;
        this.y = a.y + y;
        this.z = a.z + z;

        return this;
    }

    /**
     * Set this vector to a - b.
     */
    public final Vec3 setSub(Vec3 a, Vec3 b) {
        x = a.x - b.x;
        y = a.y - b.y;
        z = a.z - b.z;

        return this;
    }



    /**
     * Set this vector to the cross product of itself and another vector.
     *
     * @param other The right hand side vector.
     * @return cross product of this x other
     */
    public final Vec3 cross(Vec3 other) {
        return setCross(this, other);
    }

    /**
     * Set this vector to the cross product of two vectors.
     *
     * @param left The left hand side vector.
     * @param right The right hand side vector.
     * @return cross product of left x right
     */
    public final Vec3 setCross(Vec3 left, Vec3 right) {
        set(left.y  * right.z - left.z  * right.y,
            right.x * left.z  - right.z * left.x,
            left.x  * right.y - left.y  * right.x);
        return this;
    }


    public final float lengthSquared() {
        return x*x + y*y + z*z;
    }

    public final float length() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }


    /**
     * Interpolate this vector between this and the other vector.
     *
     * @param amount 0 = this vector, 1 = other vector.
     * @param v vector to interpolate towards.
     * @return this vector
     */
    public Vec3 interpolate(float amount, Vec3 v) {
        x += amount * (v.x - x);
        y += amount * (v.y - y);
        z += amount * (v.z - z);
        return this;
    }

    /**
     * Sets this vector to the linear interpolation between a and b.
     *
     * @param amount 0 = a, 1 = b.
     * @return this vector
     */
    public Vec3 setInterpolated(float amount, Vec3 a, Vec3 b) {
        x = a.x + amount * (b.x - a.x);
        y = a.y + amount * (b.y - a.y);
        z = a.z + amount * (b.z - a.z);
        return this;
    }


    /**
     * Normalizes this vector.
     */
    public final Vec3 normalize() {
        float l = length();

        // Handle zero by creating a zero normal..  Should maybe create a valid normal instead?
        if (l == 0) set(0,0,0);
        else set(x / l, y / l, z / l);

        return this;
    }

    /**
     * Sets this vector to normalized v.
     */
    public final Vec3 setNormalized(Vec3 v) {
        return set(v).normalize();
    }



    /**
     * @return the dot product of this vector and another vector.
     */
    public final float dot(Vec3 a) {
        return x * a.x +
               y * a.y +
               z * a.z;
    }

    /**
     * @return the dot product of two vectors using the formula a.x * b.x + a.y * b.y + a.z * b.z.
     */
    public static float dot(Vec3 a, Vec3 b) {
        return a.x * b.x +
               a.y * b.y +
               a.z * b.z;
    }

    /**
     * @return the angle between this and another vector, in radians.
     */
    public final float angle(Vec3 a) {
        return angle(this, a);
    }

    /**
     * @return the angle between two vectors, in radians
     */
    public static float angle(Vec3 a, Vec3 b) {
        final float divisor = a.length() * b.length();

        // Handle case of zero length vectors, assume zero angle.
        if (divisor == 0) return 0;

        float f = dot(a, b) / divisor;

        if (f < -1f) f = -1f;
        else if (f > 1f) f = 1f;

        return (float)Math.acos(f);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Vec3{");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append(", ");
        sb.append(z);
        sb.append('}');
        return sb.toString();
    }

    public Vec3 load(FloatBuffer buf) {
        x = buf.get();
        y = buf.get();
        z = buf.get();
        return this;
    }

    public Vec3 store(FloatBuffer buf) {
        buf.put(x);
        buf.put(y);
        buf.put(z);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (Float.compare(vec3.x, x) != 0) return false;
        if (Float.compare(vec3.y, y) != 0) return false;
        if (Float.compare(vec3.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
