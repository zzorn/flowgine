package org.skycastle.flowgine.buildlang.ast;

import org.parboiled.support.Var;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Prog implements Node{
    private final List<Import> imports = new ArrayList<Import>();
    private final List<Var> variables = new ArrayList<Var>();
    private final List<Fun> functions = new ArrayList<Fun>();

    public Prog() {
    }

    public boolean addImport(Import anImport) {
        imports.add(anImport);
        return true;
    }

    public boolean addVar(Var var) {
        variables.add(var);
        return true;
    }

    public boolean addFun(Fun fun) {
        functions.add(fun);
        return true;
    }

}
