package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class TypeRef implements Node {
    private final String typeName;

    public TypeRef(String typeName) {
        this.typeName = typeName;
    }
}
