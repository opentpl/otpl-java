package org.opentpl.opil;

import org.opentpl.Context;
import org.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Block extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0C;

    private String blockId;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        blockId = loader.readString();

        loader.putBlock(blockId, this);

    }

    @Override
    public int execute(Context context) throws Exception {
        context.exec(this.getLoader(), ptr() + 1);
        return -1;
    }
}
