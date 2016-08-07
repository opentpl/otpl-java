package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Reference extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0E;

    static final byte TYPE_INCLUDE = 0x01;
    static final byte TYPE_REQUIRE = 0x02;
    static final byte TYPE_LAYOUT = 0x03;


    private String src;
    private byte type;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        type = loader.readByte();
        src = loader.readString();
    }

    @Override
    public int execute(Context context) throws Exception {
        Loader loader = context.getLoader(src, getLoader().getViewName());

        switch (type) {
            case TYPE_INCLUDE:
                if (loader != null) {
                    context.exec(loader, 0);
                }
                break;
            case TYPE_LAYOUT:
                if (loader == null) {
                    throw new IllegalArgumentException("布局文件未找到或不可用：" + src);
                }
                loader.setBody(getLoader(), ptr() + 1); //必须跳过当前标签，否则无限循环
                context.exec(loader, loader.getStartPtr());
                return -1;
            case TYPE_REQUIRE:
                //什么也不做，在打开loader的时候会自动载入头
                break;
            default:
                throw new IllegalArgumentException("未定义引用资源类型：" + type);
        }

        return ptr() + 1;
    }
}
