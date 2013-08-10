package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class Num implements Expr {

    private final double value;

    public Num(double value) {
        this.value = value;
    }
}
