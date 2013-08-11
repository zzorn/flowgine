package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Assign extends Statement {
    private final String variableName;
    private final Expr expression;

    public Assign(String variableName, Expr expression) {
        this.variableName = variableName;
        this.expression = expression;
    }
}
