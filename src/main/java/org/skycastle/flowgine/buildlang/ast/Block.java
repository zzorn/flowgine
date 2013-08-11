package org.skycastle.flowgine.buildlang.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Block of statements
 */
public class Block implements Node {

    private final List<Statement> statements = new ArrayList<Statement>();

    public Block() {
    }

    public Block(Statement statement) {
        addStatement(statement);
    }



    public boolean addStatement(Statement statement) {
        statements.add(statement);

        return true;
    }

}
