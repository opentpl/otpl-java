package com.opentpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.nio.ch.FileChannelImpl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;


/**
 * Buffer工具类
 *
 * @author Jun Hwong
 */
public final class BufferUtils {
    private static final Log log = LogFactory.getLog(BufferUtils.class);
    private static Method getCleanerMethod;
    private static Method unmap;
    static {
        try {
            unmap = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            unmap.setAccessible(true);
//            getCleanerMethod = MappedByteBuffer.class.getMethod("clear", new Class[0]);//cleaner
//            getCleanerMethod.setAccessible(true);
        } catch (Throwable e) {
            log.warn("Failed to get method getCleanerMethod:", e);
        }
    }

    /**
     * 清除 MappedByteBuffer 。
     *
     * @param buffer
     */
    public static void clearBuffer(MappedByteBuffer buffer) {
        if (buffer == null) {
            return;
        }
        AccessController.doPrivileged(new PrivilegedAction() {
            @Override
            public Object run() {
                try {
                    unmap.invoke(buffer, buffer);
//                    System.out.println("clean===============");
                    //sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                    //cleaner.clean();
                } catch (Throwable e) {
                    log.error("Failed to clean buffer:", e);
                }
                return null;
            }
        });

    }


    public static byte[] toBytes(short s) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (s & 0xff);
        bytes[1] = (byte) ((s >> 8) & 0xff);
        return bytes;
    }

    public static short toShort(byte[] bytes, int index) {

        return ByteBuffer.wrap(bytes, index, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        //return (short) ((0xff & bytes[index]) | (0xff & (bytes[index + 1] << 8)));
    }

    public static byte[] toBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xff);
        bytes[1] = (byte) ((i & 0xff00) >> 8);
        bytes[2] = (byte) ((i & 0xff0000) >> 16);
        bytes[3] = (byte) ((i & 0xff000000) >> 24);
        return bytes;
    }

    public static int toInt(byte[] bytes, int index) {
        return (0xff & bytes[index])
                | (0xff00 & (bytes[index + 1] << 8))
                | (0xff0000 & (bytes[index + 2] << 16))
                | (0xff000000 & (bytes[index + 3] << 24));
    }

    public static byte[] toBytes(long l) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (l & 0xff);
        bytes[1] = (byte) ((l >> 8) & 0xff);
        bytes[2] = (byte) ((l >> 16) & 0xff);
        bytes[3] = (byte) ((l >> 24) & 0xff);
        bytes[4] = (byte) ((l >> 32) & 0xff);
        bytes[5] = (byte) ((l >> 40) & 0xff);
        bytes[6] = (byte) ((l >> 48) & 0xff);
        bytes[7] = (byte) ((l >> 56) & 0xff);
        return bytes;
    }

    public static long toLong(byte[] bytes, int index) {
        return (0xffL & (long) bytes[index])
                | (0xff00L & ((long) bytes[index + 1] << 8))
                | (0xff0000L & ((long) bytes[index + 2] << 16))
                | (0xff000000L & ((long) bytes[index + 3] << 24))
                | (0xff00000000L & ((long) bytes[index + 4] << 32))
                | (0xff0000000000L & ((long) bytes[index + 5] << 40))
                | (0xff000000000000L & ((long) bytes[index + 6] << 48))
                | (0xff00000000000000L & ((long) bytes[index + 7] << 56));
    }

    public static byte[] toBytes(double d) {
        return toBytes(Double.doubleToLongBits(d));
    }

    public static double toDouble(byte[] bytes, int index) {
        return Double.longBitsToDouble(toLong(bytes, index));
    }

    public static byte[] toBytes(float f) {
        return toBytes(Float.floatToIntBits(f));
    }

    public static float toFloat(byte[] bytes, int index) {
        return Float.intBitsToFloat(toInt(bytes, index));
    }


    public static String md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


}
