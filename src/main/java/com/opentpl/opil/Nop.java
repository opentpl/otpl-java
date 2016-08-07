package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Nop extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x02;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
    }

    @Override
    public int execute(Context context) throws Exception {
        return ptr() + 1;
    }

}
