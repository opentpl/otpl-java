package com.opentpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jun Hwong
 */
public class BufferUtilsTest {
    @Test
    public void testMD5() throws Exception {


        Assert.assertEquals(BufferUtils.md5("BufferUtilsTest"), "84264f1493a14bf82199111d9fc30c07");

    }

    @Test
    public void testToNumber() {


    }


}