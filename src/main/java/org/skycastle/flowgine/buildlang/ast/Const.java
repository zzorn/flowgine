package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Const implements Node {
    private final TypeRef type;
    private final String name;
    private final Expr expr;

    public Const(TypeRef type, String name, Expr expr) {
        this.type = type;
        this.name = name;
        this.expr = expr;
    }
}
