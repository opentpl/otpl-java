package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Scope extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0B;

    private boolean mode;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        mode = loader.readBoolean();
    }

    @Override
    public int execute(Context context) throws Exception {
        if (mode) {
            context.scope();
        } else {
            context.unscope();
        }
        return ptr() + 1;
    }
}
