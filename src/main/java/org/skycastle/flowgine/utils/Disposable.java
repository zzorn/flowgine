package org.skycastle.flowgine.utils;

/**
 * Represents some object that can free resources it is using.
 */
public interface Disposable {

    /**
     * Release any reserved resources.
     */
    void dispose();

}
