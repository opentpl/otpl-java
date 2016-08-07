package com.opentpl.opil;

/**
 * @author Jun Hwong
 */
public enum Operator {
    /**
     * 相加
     */
    Add(0x01),
    /**
     * 相减
     */
    Sub(0x02),
    /**
     * 相乘
     */
    Mul(0x03),
    /**
     * 相除
     */
    Div(0x04),
    /**
     * 取模
     */
    Mod(0x05),
    /**
     * 取负
     */
    Neg(0x06),
    /**
     * 取正
     */
    Pos(0x07),
    /**
     * 逻辑等于
     */
    Eq(0x08),
    /**
     * 逻辑不等于
     */
    Ne(0x09),
    /**
     * 逻辑大于
     */
    Gt(0x0A),
    /**
     * 逻辑大于等于
     */
    Ge(0x0B),
    /**
     * 逻辑小于
     */
    Lt(0x0C),
    /**
     * 逻辑小于等于
     */
    Le(0x0D),
    //Leg(14),
    /**
     * 逻辑与
     */
    And(0x0E),
    /**
     * 逻辑或
     */
    Or(0x0F);

    private byte value;


    Operator(int value) {
        this.value = (byte) value;
    }

    public static Operator valueOf(byte value) {
        for (Operator op : Operator.values()) {
            if (op.value == value) {
                return op;
            }
        }
        return null;
    }

}
