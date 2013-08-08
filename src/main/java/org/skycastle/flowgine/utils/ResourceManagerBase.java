package org.skycastle.flowgine.utils;

import org.skycastle.flowgine.FlowGine;
import org.skycastle.flowgine.resourceloader.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for resource managers.
 *
 * @param <R> reference type.
 * @param <T> resource type
 */
public abstract class ResourceManagerBase<R, T extends Disposable> {

    private final Map<R, T> resources = new HashMap<R, T>();
    private final Map<T, R> resourcesRefs = new HashMap<T, R>();
    private final Map<R, Integer> resourceUsers = new HashMap<R, Integer>();

    private T placeholder = null;

    /**
     * Retrieves or loads the specified resource.
     * @param ref reference to the resource to get.
     * @return the resource, or a placeholder if it could not be found and a placeholder was configured.
     * @throws IllegalArgumentException if the specified resource could not be found, and there was no placeholder configured.
     */
    public final T get(R ref) {
        // Get resource if already created
        T resource = resources.get(ref);
        if (resource == null) {
            try {
                // Create resource
                resource = createResource(ref, getResourceLoader());

                // Check for openGL errors
                OpenGLUtils.checkGLError("creating resource '"+ref+"'");

                // Remember reference for this resource (if it was not the placeholder)
                resourcesRefs.put(resource, ref);
            }
            catch (Throwable e) {
                final String message = "Could not load resource '" + ref + "': " + e.getMessage();

                // Use placeholder resource if specified
                if (placeholder != null) {
                    resource = placeholder;
                    System.err.print(message + "\nUsing placeholder resource instead.");
                }
                else {
                    // No placeholder, throw exception
                    throw new IllegalArgumentException(message, e);
                }
            }

            // Store created resource (also if it was the placeholder, to avoid trying a failing re-create each time it is requested).
            resources.put(ref, resource);
        }

        // Keep track of number of resource users.
        increaseUsages(ref);

        return resource;
    }

    /**
     * Should be called when a resource is no longer needed.
     * If no-one is using the resource it will be deleted.
     * @param resource resource to release.
     */
    public final void release(T resource) {
        releaseByRef(getRef(resource));
    }

    /**
     * Should be called when a resource is no longer needed.
     * If no-one is using the resource it will be deleted.
     *
     * @param ref reference of resource to release.
     */
    public final void releaseByRef(R ref) {
        decreaseUsages(ref);
    }

    /**
     * @return the current placeholder resource, or null if none configured.
     */
    public final T getPlaceholder() {
        return placeholder;
    }

    /**
     * @param placeholder a placeholder resource to use if loading a resource fails,
     *                    or null for none (in which case we throw an exception if a resource could not be loaded).
     */
    public final void setPlaceholder(T placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * @param placeholderRef reference to a placeholder resource to use if loading a resource fails,
     *                    or null for none (in which case we throw an exception if a resource could not be loaded).
     */
    public final void setPlaceholderFromRef(R placeholderRef) {
        placeholder = null;
        try {
            placeholder = createResource(placeholderRef, getResourceLoader());
        } catch (Exception e) {
            throw new IllegalStateException("Could not load placeholder resource '" + placeholderRef + "': " + e.getMessage(), e);
        }
    }

    /**
     * Deletes all stored resources
     */
    public final void deleteAll() {
        boolean placeholderDeleted = false;
        for (T resource : resources.values()) {
            // Only delete the placeholder once.
            if (!(resource == placeholder && placeholderDeleted)) {
                deleteResource(resource);
            }

            if (resource == placeholder) placeholderDeleted = true;
        }

        if (placeholder != null && !placeholderDeleted) {
            deleteResource(placeholder);
        }

        resources.clear();
        resourcesRefs.clear();
        resourceUsers.clear();
        placeholder = null;

        // Check for openGL errors
        OpenGLUtils.checkGLError("deleting all resources in " + getClass().getName());
    }

    /**
     * Create a resource based on a reference.
     *
     * @param ref reference to create a resource for.
     * @param resourceLoader resource loader that can be used to load resource files with.
     * @return created resource.
     * @throws Exception if there was some problem, the placeholder resource will be used instead, if it is configured.
     */
    protected abstract T createResource(R ref, ResourceLoader resourceLoader) throws Exception;

    /**
     * @return resource loader to use when loading resources.  Defaults to FlowGine.resourceLoader.
     */
    protected ResourceLoader getResourceLoader() {
        return FlowGine.resourceLoader;
    }

    /**
     * @return the reference of the specified resource.
     */
    private R getRef(T resource) {
        return resourcesRefs.get(resource);
    }

    /**
     * Frees any memory or graphics card resources used by the specified resource.
     */
    private void deleteResource(T resource) {
        resource.dispose();
    }


    private void increaseUsages(R ref) {
        // Get current user count for the resource
        Integer usageCount = resourceUsers.get(ref);
        if (usageCount == null) usageCount = 0;

        // Increase user count.
        usageCount++;
        resourceUsers.put(ref, usageCount);
    }

    private void decreaseUsages(R ref) {
        if (ref != null) {
            // Get current user count for the resource
            Integer usageCount = resourceUsers.get(ref);
            if (usageCount == null) usageCount = 0;

            // Decrease user count.
            usageCount--;
            resourceUsers.put(ref, usageCount);

            // Check if we should free the resource
            if (usageCount <= 0) {
                resourceUsers.remove(ref);
                final T resource = resources.remove(ref);

                // Placeholder should not be deleted
                if (resource != null && resource != placeholder) {
                    resourcesRefs.remove(resource);
                    deleteResource(resource);

                    // Check for openGL errors
                    OpenGLUtils.checkGLError("deleting resource '"+ref+"'");
                }
            }
        }
    }



}
