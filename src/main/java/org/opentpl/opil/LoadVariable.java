package org.opentpl.opil;

import org.opentpl.Context;
import org.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class LoadVariable extends Opcode {


    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x05;

    private String name;


    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        name = loader.readString();

    }

    @Override
    public int execute(Context context) throws Exception {
        context.push(context.getVariable(name));
        return ptr() + 1;
    }
}
