package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Convert;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Break extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x03;

    public static final byte TYPE_EXIT = 0X01;
    public static final byte TYPE_NEVER = 0X02;
    public static final byte TYPE_TRUE = 0X03;
    public static final byte TYPE_FALSE = 0X04;

    private byte breakType;
    private int target;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        breakType = loader.readByte();
        switch (breakType) {
            case TYPE_EXIT:
                break;
            case TYPE_FALSE:
            case TYPE_NEVER:
            case TYPE_TRUE:
                target = loader.readInt();
                break;
            default:
                throw new IllegalStateException("中断类型未定义：" + breakType);
        }
    }

    boolean getBool(Object a) {
        Boolean b = Convert.toBoolean(a, false);
        if (b == null) {
            Number n = Convert.toNumber(a, true, false);
            if (n != null) {
                b = n.doubleValue() != 0;
            } else {
                b = a != null;
            }
        }
        return b;
    }

    @Override
    public int execute(Context context) throws Exception {
        switch (breakType) {
            case TYPE_EXIT:
                return -1;
            case TYPE_FALSE: {

                if (!getBool(context.pop())) {
                    return target;
                }
                break;
            }
            case TYPE_NEVER:
                return target;
            case TYPE_TRUE: {
                if (getBool(context.pop())) {
                    return target;
                }
                break;
            }
            default:
                throw new IllegalStateException("中断类型未定义：" + breakType);
        }
        return ptr() + 1;
    }
}
