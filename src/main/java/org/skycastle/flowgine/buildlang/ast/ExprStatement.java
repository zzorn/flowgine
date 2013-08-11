package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class ExprStatement extends Statement {
    private final Expr expr;

    public ExprStatement(Expr expr) {
        this.expr = expr;
    }
}
