package org.skycastle.flowgine.geometry;

import java.nio.FloatBuffer;

/**
 * Color class extending 4 dimensional vector class.
 */
// TODO: Do not extend Vector4f
public final class Col4 {

    public float r = 0;
    public float g = 0;
    public float b = 0;
    public float a = 1;

    public float getRed()   {return r;}
    public float getGreen() {return g;}
    public float getBlue()  {return b;}
    public float getAlpha() {return a;}

    public void setRed(float   red)   {r = red;}
    public void setGreen(float green) {g = green;}
    public void setBlue(float  blue)  {b = blue;}
    public void setAlpha(float alpha) {a = alpha;}

    /**
     * Creates a black opaque color.
     */
    public Col4() {
    }

    public Col4(float red, float green, float blue) {
        r = red;
        g = green;
        b = blue;
    }

    public Col4(float red, float green, float blue, float alpha) {
        r = red;
        g = green;
        b = blue;
        a = alpha;
    }

    public Col4 set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        return this;
    }

    public Col4 set(Col4 color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;

        return this;
    }

    public Col4 scale(float scale) {
        r *= scale;
        g *= scale;
        b *= scale;
        a *= scale;
        return this;
    }

    public Col4 add(Col4 c) {
        r += c.r;
        g += c.g;
        b += c.b;
        a += c.a;
        return this;
    }

    /**
     * Interpolate this color between this and the other color.
     *
     * @param amount 0 = this color, 1 = other color.
     * @param c color to interpolate towards.
     * @return this color
     */
    public Col4 interpolate(float amount, Col4 c) {
        r += amount * (c.r - r);
        g += amount * (c.g - g);
        b += amount * (c.b - b);
        a += amount * (c.a - a);
        return this;
    }

    public void setFromHSL(float hue, float sat, float lum, float alpha) {
        if (sat == 0.0F) {
            r = g = b = lum;
        } else {
            float f3 = (hue - (float) Math.floor(hue)) * 6F;
            float f4 = f3 - (float) Math.floor(f3);
            float f5 = lum * (1.0F - sat);
            float f6 = lum * (1.0F - sat * f4);
            float f7 = lum * (1.0F - sat * (1.0F - f4));
            switch ((int) f3) {
                case 0 :
                    r = lum;
                    g = f7;
                    b = f5;
                    break;
                case 1 :
                    r = f6;
                    g = lum;
                    b = f5;
                    break;
                case 2 :
                    r = f5;
                    g = lum;
                    b = f7;
                    break;
                case 3 :
                    r = f5;
                    g = f6;
                    b = lum;
                    break;
                case 4 :
                    r = f7;
                    g = f5;
                    b = lum;
                    break;
                case 5 :
                    r = lum;
                    g = f5;
                    b = f6;
                    break;
            }
        }

        clampToZeroToOne();

        a = alpha;
    }

    public Col4 clampToZeroToOne() {
        r = clampZeroToOne(r);
        g = clampZeroToOne(g);
        b = clampZeroToOne(b);
        a = clampZeroToOne(a);
        return this;
    }

    private float clampZeroToOne(float v) {
        if (v < 0) return 0;
        else if (v > 1) return 1;
        else return v;
    }

    public Col4 load(FloatBuffer buf) {
        r = buf.get();
        g = buf.get();
        b = buf.get();
        a = buf.get();
        return this;
    }

    public Col4 store(FloatBuffer buf) {
        buf.put(r);
        buf.put(g);
        buf.put(b);
        buf.put(a);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Col4 col4 = (Col4) o;

        if (Float.compare(col4.a, a) != 0) return false;
        if (Float.compare(col4.b, b) != 0) return false;
        if (Float.compare(col4.g, g) != 0) return false;
        if (Float.compare(col4.r, r) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
        result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
        result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
        result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
        return result;
    }

    @Override public String toString() {
        return "Color{"+r+", "+g+", "+b+", "+a+"}";
    }
}
