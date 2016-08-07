package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Print extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x08;

    private boolean escape;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        escape = loader.readBoolean();
    }

    @Override
    public int execute(Context context) throws Exception {
        context.print(context.pop(), escape);
        return ptr() + 1;
    }
}
