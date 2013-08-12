package org.skycastle.flowgine.buildlang.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ListExpr implements Expr {
    private final List<Expr> elements = new ArrayList<Expr>();

    public ListExpr() {
    }

    public boolean add(Expr element) {
        elements.add(element);
        return true;
    }


}
