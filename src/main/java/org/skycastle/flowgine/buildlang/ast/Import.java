package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Import implements Node {
    private final String importRef;

    public Import(String importRef) {
        this.importRef = importRef;
    }
}
