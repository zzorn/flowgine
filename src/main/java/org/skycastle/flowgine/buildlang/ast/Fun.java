package org.skycastle.flowgine.buildlang.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Fun  implements Node{

    private final String name;
    private final TypeRef typeRef;
    private final List<Param> parameters = new ArrayList<Param>();
    private final Block code;

    public Fun(String name, TypeRef typeRef, List<Param> parameters, Block code) {
        this.name = name;
        this.typeRef = typeRef;
        this.parameters.addAll(parameters);
        this.code = code;
    }
}
