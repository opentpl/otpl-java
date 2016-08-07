package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Convert;
import com.opentpl.Loader;

/**
 * @author Jun Hwong
 */
public class Operation extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x09;

    private Operator operator;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        byte op = loader.readByte();
        operator = Operator.valueOf(op);
        if (operator == null) {
            throw new IllegalArgumentException("无效的运行符类型：" + op);
        }
    }

    @Override
    public int execute(Context context) throws Exception {
        Object a, b;

        switch (operator) {
            case Add: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() + n2.doubleValue());
                    } else {
                        context.push(n1.longValue() + n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Sub: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() - n2.doubleValue());
                    } else {
                        context.push(n1.longValue() - n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Mul: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() * n2.doubleValue());
                    } else {
                        context.push(n1.longValue() * n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Div: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() / n2.doubleValue());
                    } else {
                        context.push(n1.longValue() / n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Mod: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() % n2.doubleValue());
                    } else {
                        context.push(n1.longValue() % n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Neg: {

                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(-n1.doubleValue());
                    } else {
                        context.push(-n1.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Pos: {

                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(+n1.doubleValue());
                    } else {
                        context.push(+n1.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Eq: {
                b = context.pop();
                a = context.pop();

                boolean result = false;

                if (a == null && b == null) {
                    result = true;
                } else if (a != null) {
                    result = a.equals(b);
                }

                context.push(result);
                break;
            }
            case Ne: {
                b = context.pop();
                a = context.pop();

                boolean result = true;

                if (a == null && b == null) {
                    result = false;
                } else if (a != null) {
                    result = !a.equals(b);
                }

                context.push(result);
                break;
            }
            case Gt: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() > n2.doubleValue());
                    } else {
                        context.push(n1.longValue() > n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Ge: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() >= n2.doubleValue());
                    } else {
                        context.push(n1.longValue() >= n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Lt: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() < n2.doubleValue());
                    } else {
                        context.push(n1.longValue() < n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Le: {

                b = context.pop();
                a = context.pop();

                boolean hasFloat = Convert.hasFloat(a, b);

                Number n1 = Convert.toNumber(a, hasFloat, context.isStrict());
                Number n2 = Convert.toNumber(b, hasFloat, context.isStrict());

                try {
                    if (hasFloat) {
                        context.push(n1.doubleValue() <= n2.doubleValue());
                    } else {
                        context.push(n1.longValue() <= n2.longValue());
                    }
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case And: {

                b = context.pop();
                a = context.pop();

                try {
                    context.push(Convert.toBoolean(a, context.isStrict()) && Convert.toBoolean(b, context.isStrict()));
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }
            case Or: {

                b = context.pop();
                a = context.pop();

                try {
                    context.push(Convert.toBoolean(a, context.isStrict()) || Convert.toBoolean(b, context.isStrict()));
                } catch (Throwable e) {
                    if (context.isStrict()) {
                        throw e;
                    }
                    context.push(null);
                }
                break;
            }

        }

        return ptr() + 1;
    }


}
