package org.skycastle.flowgine.buildlang.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapExpr implements Expr {
    private final List<MapExprEntry> entries = new ArrayList<MapExprEntry>();

    public MapExpr() {
    }

    public boolean add(Expr key, Expr value) {
        entries.add(new MapExprEntry(key, value));
        return true;
    }


    public static class MapExprEntry {
        private final Expr key;
        private final Expr value;

        public MapExprEntry(Expr key, Expr value) {
            this.key = key;
            this.value = value;
        }

        public Expr getKey() {
            return key;
        }

        public Expr getValue() {
            return value;
        }
    }

}
