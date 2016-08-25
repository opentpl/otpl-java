package org.opentpl.opil;

import org.opentpl.Context;
import org.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class BlockCall extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0D;

    private String blockId;
    private int parameterCount;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        blockId = loader.readString();
        parameterCount = loader.readInt();
    }

    @Override
    public int execute(Context context) throws Exception {
        Loader loader = getLoader();
        if ("body".equals(blockId)) {

            if (loader.getBodyLoader() == null) {
                throw new IllegalStateException("布局页面不能直接执行，标签：@body");
            }

            context.exec(loader.getBodyLoader(), loader.getBodyPtr());
            //TODO:关注后面的执行
        } else {
            Opcode block = loader.getBlock(blockId);
            if (block != null) {
                block.execute(context);
            }
        }
        return ptr() + 1;
    }
}
