package com.opentpl;

import java.nio.charset.Charset;

/**
 * @author Jun Hwong
 */
public class CharsetUtil {

    public static final Charset UTF8 = Charset.forName("utf8");

    public static final Charset ASCII = Charset.forName("ascii");


    public static Charset valueOfCode(byte code) {
        switch (code) {
            case 0x01:
                return ASCII;
            default:
                return UTF8;
        }
    }

}
