package org.opentpl.opil;

import org.opentpl.Context;
import org.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class LoadConst extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x04;

    private DataType dataType;
    private Object value;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        byte val = loader.readByte();
        dataType = DataType.valueOf(val);
        if (dataType == null) {
            throw new IllegalStateException("数据类型未定义：" + val);
        }
        switch (dataType) {
            case NULL:
                value = null;
                break;
            case STR:
                value = loader.readString();
                break;
            case INT:
                value = loader.readInt();
                break;
            case LONG:
                value = loader.readLong();
                break;
            case FLOAT:
                value = loader.readFloat();
                break;
            case TRUE:
                value = true;
                break;
            case FLASE:
                value = false;
                break;
        }
    }

    @Override
    public int execute(Context context) throws Exception {

        context.push(value);
        return ptr() + 1;
    }
}
