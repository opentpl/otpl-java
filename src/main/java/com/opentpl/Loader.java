/*
 * Copyright (C) 2016 The OpenTPL Project. All rights reserved.
 */
package com.opentpl;

import com.opentpl.opil.Opcode;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun Hwong
 */
public class Loader {

    private static final String OTPL_NAME = "OTIL";
    private MappedByteBuffer buffer;
    private FileChannel channel;
    private Charset encoding = CharsetUtil.UTF8;
    private byte[] buf;
    private IntBuffer intBuffer;
    private Map<Integer, Opcode> codes = new HashMap<>();
    private long modified;
    private String viewName;
    private int startPtr;
    private Map<String, Opcode> blocks = new HashMap<>();
    private Loader bodyLoader;
    private int bodyStartPtr;

    private Loader() {
        buf = new byte[8];
        intBuffer = ByteBuffer.wrap(buf, 0, 4).asIntBuffer();
    }


    /**
     * 打开一个OTIL文件
     *
     * @param file il file
     * @throws Exception
     */
    public static Loader open(File file) throws Exception {

        Loader loader = new Loader();

        loader.channel = new RandomAccessFile(file, "r").getChannel();
        loader.buffer = loader.channel.map(FileChannel.MapMode.READ_ONLY, 0, loader.channel.size());
        loader.buffer.order(ByteOrder.BIG_ENDIAN);

        loader.loadHeader();

        loader.loadToPtr(loader.startPtr);

        return loader;
    }

    /**
     * 关闭载入器的IO相关资源。
     */
    public void close() {
        try {
            channel.close();
        } catch (Throwable e) {
        } finally {
            BufferUtils.clearBuffer(buffer);
            buffer = null;
            channel = null;
        }

    }

    /**
     *
     */
    public void destory() {
        close();
        codes.clear();
        blocks.clear();
        bodyLoader = null;
    }


    public byte readByte() {
        return buffer.get();
    }

    public boolean readBoolean() {
        return buffer.get() != 0x00;
    }

    public int readInt() {

        intBuffer.clear();

        buffer.get(buf, 0, 4);

        return intBuffer.get();
    }

    public long readLong() {
        return Long.parseLong(readString()); //解决nodejs不能处理64位数值的问题。
//        buffer.get(buf, 0, 8);
//        return BufferUtils.toLong(buf, 0);
    }

    public double readFloat() {
        return Double.parseDouble(readString()); //解决nodejs不能处理64位数值的问题。
//        buffer.get(buf, 0, 8);
//        return BufferUtils.toLong(buf, 0);
    }

    public String readString(Charset charset) {
        int len = readInt();
        byte[] buf = new byte[len];
        buffer.get(buf, 0, len);
        return new String(buf, 0, len, charset);
    }

    public String readString() {
        return readString(encoding);
    }


    void loadHeader() throws Exception {

        buffer.get(buf, 0, 8);
        String name = new String(buf, 0, 4, encoding);  //名称 OPIL
        byte version = buf[4];                          //版本
        encoding = CharsetUtil.valueOfCode(buf[5]);       //字符编码
        //保留2位

        modified = readLong();                          //源文件修改时间 8字节
        viewName = readString();                        //源文件相对标准路径
        startPtr = readInt();                           //头结束地址 4字节

        if (OTPL_NAME.equals(name)) {
            throw new IllegalStateException("不是一个有效果的OTIL");
        }

        if (version <= 0) {
            throw new IllegalStateException("OTIL版本不支持");
        }

    }


    void loadToPtr(int ptr) throws Exception {

        while (true) {

            if (codes.containsKey(ptr) || !buffer.hasRemaining()) {
                break;
            }

            //TODO: 到达文件结束，或IO异常处理
            Opcode code = Opcode.loadCode(readInt(), buffer.get(), this);

            if (code == null) {
                return;
            }

            if (codes.containsKey(code.ptr())) {
                break;//TODO: 重复行异常
            }

            codes.put(code.ptr(), code);


        }

    }

    public Opcode load(int ptr) throws Exception {
        loadToPtr(ptr);
        return codes.getOrDefault(ptr, null);
    }


    public int getStartPtr() {
        return startPtr;
    }

    public String getViewName() {
        return viewName;
    }

    public void putBlock(String blockId, Opcode code) {
        if (blockId == null) {
            throw new IllegalArgumentException("blockId");
        }
        if (code == null) {
            throw new IllegalArgumentException("code");
        }
        blocks.put(blockId.trim().toLowerCase(), code);
    }

    public Opcode getBlock(String blockId) {

        if (blockId == null) {
            throw new IllegalArgumentException("blockId");
        }

        return blocks.getOrDefault(blockId.trim().toLowerCase(), null);
    }

    public Loader getBodyLoader() {
        return bodyLoader;
    }

    public int getBodyPtr() {
        return bodyStartPtr;
    }

    public void setBody(Loader bodyLoader, int bodyPtr) {
        if (bodyLoader == null) {
            throw new IllegalArgumentException("bodyLoader");
        }
        this.bodyLoader = bodyLoader;
        this.bodyStartPtr = bodyPtr;
    }
}
