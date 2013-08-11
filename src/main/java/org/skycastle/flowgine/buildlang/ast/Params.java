package org.skycastle.flowgine.buildlang.ast;

import org.flowutils.Check;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Params implements Node {
    private final List<Param> params = new ArrayList<Param>();

    public Params() {
    }

    /**
     * Adds a parameter.
     * @return we need to return something for usage in rules, so return true.
     */
    public boolean add(Param param) {
        params.add(param);

        return true;
    }

    /**
     * Adds a parameter.
     * @return we need to return something for usage in rules, so return true.
     */
    public Param getLast() {
        Check.notEmpty(params, "params");

        return params.get(params.size() - 1);
    }

    public List<Param> getParams() {
        return params;
    }
}
