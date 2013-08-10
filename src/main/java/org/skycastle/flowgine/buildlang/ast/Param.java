package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Param implements Node {
    private final TypeRef typeRef;
    private final String name;
    private Expr defaultValue;

    public Param(TypeRef typeRef, String name) {
        this.typeRef = typeRef;
        this.name = name;
    }

    public Expr getDefaultValue() {
        return defaultValue;
    }

    public Param setDefaultValue(Expr defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
