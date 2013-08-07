package org.skycastle.flowgine.spatial;

import org.skycastle.flowgine.utils.OpenGLUtils;

/**
 *
 */
public abstract class AbstractSpatial implements Spatial {
    private boolean isCreated = false;

    public final void create() {
        if (!isCreated) {
            doCreate();
            OpenGLUtils.checkGLError("Creating spatial " + this);
            isCreated = true;
        }
    }

    public final void update(double deltaSeconds, double gameTimeSeconds) {
        if (!isCreated) create();

        doUpdate(deltaSeconds, gameTimeSeconds);

        OpenGLUtils.checkGLError("Updating spatial " + this);
    }

    public final void render() {
        if (!isCreated) create();

        doRender();

        OpenGLUtils.checkGLError("Rendering spatial " + this);
    }

    public final void delete() {
        if (isCreated) {
            doDelete();

            OpenGLUtils.checkGLError("Deleting spatial " + this);

            isCreated = false;
        }
    }

    public final boolean isCreated() {
        return isCreated;
    }

    protected abstract void doCreate();
    protected abstract void doUpdate(double deltaSeconds, double gameTimeSeconds);
    protected abstract void doRender();
    protected abstract void doDelete();
}
