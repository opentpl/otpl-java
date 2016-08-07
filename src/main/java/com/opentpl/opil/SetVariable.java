package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class SetVariable extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x06;

    private String name;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        name = loader.readString();
    }

    @Override
    public int execute(Context context) throws Exception {
        context.setVariable(name, context.pop());
        return ptr() + 1;
    }
}
