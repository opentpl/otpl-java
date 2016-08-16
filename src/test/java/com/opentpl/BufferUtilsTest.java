package com.opentpl;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;

/**
 * @author Jun Hwong
 */
public class BufferUtilsTest {
    @Test
    public void testMD5() throws Exception {


        Assert.assertEquals(BufferUtils.md5("BufferUtilsTest"), "84264f1493a14bf82199111d9fc30c07");

    }

    @Test
    public void testClearBuffer() {
        BufferUtils.clearBuffer(null);

        Method[] methods = MappedByteBuffer.class.getMethods();

        for (Method method : methods) {
            if ("clear".equals(method.getName())) {
                System.out.println("");
            }
            System.out.println(method.getName());
        }
        System.out.println(methods.length);
    }

    @Test
    public void testToNumber() {

    }


}