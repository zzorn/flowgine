package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class While extends Statement {
    private final BoolExpr condition;
    private final Block block;

    public While(BoolExpr condition, Block block) {
        this.condition = condition;
        this.block = block;
    }
}
