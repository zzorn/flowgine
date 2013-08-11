package org.skycastle.flowgine.buildlang.ast;

/**
 *
 */
public class VarDef extends Statement {
    private TypeRef type;
    private boolean constant = false;
    private String name;
    private Expr expr;

    public VarDef() {
    }

    public VarDef(TypeRef type, boolean constant, String name, Expr expr) {
        this.type = type;
        this.constant = constant;
        this.name = name;
        this.expr = expr;
    }

    public TypeRef getType() {
        return type;
    }

    public boolean setType(TypeRef type) {
        this.type = type;
        return true;
    }

    public boolean isConstant() {
        return constant;
    }

    public boolean setConstant(boolean constant) {
        this.constant = constant;
        return true;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public Expr getExpr() {
        return expr;
    }

    public boolean setExpr(Expr expr) {
        this.expr = expr;
        return true;
    }
}
