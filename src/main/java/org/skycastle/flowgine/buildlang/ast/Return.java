package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Return extends Statement {

    private final Expr expr;

    public Return(Expr expr) {
        this.expr = expr;
    }
}
