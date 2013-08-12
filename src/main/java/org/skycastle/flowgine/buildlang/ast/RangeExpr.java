package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class RangeExpr implements Expr {

    private final Expr start;
    private final Expr end;
    private final boolean inclusive;
    private Expr step = null;

    public RangeExpr(Expr start, Expr end, boolean inclusive) {
        this.start = start;
        this.end = end;
        this.inclusive = inclusive;
    }

    public boolean setStep(Expr step) {
        this.step = step;
        return true;
    }


}
