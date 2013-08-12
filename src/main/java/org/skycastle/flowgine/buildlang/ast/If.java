package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class If implements Expr {
    private final BoolExpr condition;
    private final Block thenBlock;
    private Block elseBlock = null;

    public If(BoolExpr condition, Block thenBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
    }

    public boolean setElseBlock(Block elseBlock) {
        this.elseBlock = elseBlock;
        return true;
    }
}
