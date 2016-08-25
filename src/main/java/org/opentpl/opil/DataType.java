package org.opentpl.opil;

/**
 * @author Jun Hwong
 */
public enum DataType {
    /**
     * 空值
     */
    NULL(0X00),
    /**
     * 字符串
     */
    STR(0x01),
    /**
     * 整形
     */
    INT(0x02),
    /**
     * 长整形
     */
    LONG(0x03),
    /**
     * 浮点值
     */
    FLOAT(0x04),
    /**
     * 真
     */
    TRUE(0x05),
    /**
     * 假
     */
    FLASE(0x06);


    private byte value;

    DataType(int value) {
        this.value = (byte) value;
    }

    public static DataType valueOf(byte value) {
        for (DataType dataType : DataType.values()) {
            if (dataType.value == value) {
                return dataType;
            }
        }
        return null;
    }

}
