/*
 * Copyright (C) 2016 The OpenTPL Project. All rights reserved.
 */
package org.opentpl.opil;

import org.opentpl.Context;
import org.opentpl.Loader;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun Hwong
 */
public abstract class Opcode {

    protected static void TEST() {
        System.out.println("xxxx");
    }

    private static final Map<Byte, Class<? extends Opcode>> codeTypes = new HashMap<>();

    protected static void register(int codeType, Class<? extends Opcode> clazz) {
        if (codeTypes.containsKey(codeType)) {
            throw new IllegalStateException("操作码 " + codeType + " 已经注册为:[" + codeTypes.get(codeType) + "]，不能重复注册：[" + clazz + "]");
        }
        codeTypes.put((byte) codeType, clazz);
    }

    static {
        Opcode.register(Nop.CODE, Nop.class);
        Opcode.register(Break.CODE, Break.class);
        Opcode.register(LoadConst.CODE, LoadConst.class);
        Opcode.register(LoadVariable.CODE, LoadVariable.class);
        Opcode.register(SetVariable.CODE, SetVariable.class);
        Opcode.register(Call.CODE, Call.class);
        Opcode.register(Print.CODE, Print.class);
        Opcode.register(Operation.CODE, Operation.class);
        Opcode.register(LoadMember.CODE, LoadMember.class);
        Opcode.register(Scope.CODE, Scope.class);
        Opcode.register(Block.CODE, Block.class);
        Opcode.register(BlockCall.CODE, BlockCall.class);
        Opcode.register(Reference.CODE, Reference.class);
        Opcode.register(CastToIterator.CODE, CastToIterator.class);


    }


    /**
     * 根据指定参数载入操作码。
     *
     * @param ptr
     * @param codeType
     * @param loader
     * @return
     * @throws Exception
     */
    public static Opcode loadCode(int ptr, byte codeType, Loader loader) throws Exception {
        Class<? extends Opcode> clazz = codeTypes.getOrDefault(codeType, null);
        if (clazz == null) {
            throw new IllegalArgumentException("指定操作码类型未定义：" + codeType);
        }
        Opcode code = clazz.newInstance();
        code.loader = loader;
        code.load(ptr, loader);
        return code;
    }


    private int ptr;
    private Loader loader;
    private int lineNumber;

    public int ptr() {
        return ptr;
    }


    /**
     * 设置地址
     *
     * @param ptr
     */
    protected void setPtr(int ptr) {
        this.ptr = ptr;
    }

    protected void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    protected final Loader getLoader() {
        return loader;
    }

    /**
     * 根据传入的地址和loader实现代码载入。
     *
     * @param ptr
     * @param loader
     */
    protected abstract void load(int ptr, Loader loader) throws Exception;

    /**
     * 保留
     *
     * @param output
     */
    public void compile(OutputStream output) {

    }

    /**
     * 执行操作码。
     *
     * @param context
     * @return
     * @throws Exception
     */
    public abstract int execute(Context context) throws Exception;

}
