package org.skycastle.flowgine.buildlang.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
// TODO: How to model closures?
public class FunExpr implements Expr{
    private final TypeRef typeRef;
    private final List<Param> parameters = new ArrayList<Param>();
    private final Block code;

    public FunExpr(TypeRef typeRef, List<Param> parameters, Block code) {
        this.typeRef = typeRef;
        this.parameters.addAll(parameters);
        this.code = code;
    }

}
