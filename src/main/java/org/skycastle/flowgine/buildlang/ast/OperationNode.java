package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class OperationNode implements Expr {
    private final String operator;
    private final Expr left;
    private final Expr right;

    public OperationNode(Expr left, String operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


}
