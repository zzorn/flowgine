package org.skycastle.flowgine.spatial;

import org.skycastle.flowgine.utils.Disposable;

/**
 * Represents a visible 3D object.
 * Has some shape, shaders, textures, etc.
 * Can be rendered and updated, and deleted.
 */
public interface Spatial extends Disposable {

    /**
     * Called to initialize the object.
     */
    public void create();

    /**
     * Called regularly.  Allows object to animate.
     * @param deltaSeconds seconds since last update call.
     * @param gameTimeSeconds total game time so far in seconds.
     */
    public void update(double deltaSeconds, double gameTimeSeconds);

    /**
     * Render the object.
     */
    // TODO: Possibly pass transformation matrix to allow node hierarchy
    public void render();


}
