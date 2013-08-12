package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class For extends Statement {
    private final TypeRef typeRef;
    private final String iteratorName;
    private final Expr range;
    private final Block block;

    public For(TypeRef typeRef, String iteratorName, Expr range, Block block) {
        this.typeRef = typeRef;
        this.iteratorName = iteratorName;
        this.range = range;
        this.block = block;
    }
}
