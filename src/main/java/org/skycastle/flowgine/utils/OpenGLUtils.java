package org.skycastle.flowgine.utils;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * OpenGL related utilities.
 */
public final class OpenGLUtils {

    /**
     * Checks for opengl errors, if found, prints error message and exits.
     * @param errorContext context that the error occurred in, typically method or subsystem name, or description of tested activity.
     */
    public static void checkGLError(String errorContext) {
        int errorValue = GL11.glGetError();
        if (errorValue != GL11.GL_NO_ERROR) {
            System.err.println("OPENGL ERROR: When " + errorContext + ": " + GLU.gluErrorString(errorValue));

            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }


    private OpenGLUtils() {}
}
