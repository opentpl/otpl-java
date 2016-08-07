package com.opentpl;

/**
 * @author Jun Hwong
 */
public class Convert {

    public static Boolean toBoolean(Object obj, boolean strict) {

        try {
            if ("true".equalsIgnoreCase(obj.toString())) {
                return true;
            } else if ("false".equalsIgnoreCase(obj.toString())) {
                return false;
            }
            throw new IllegalArgumentException("无效boolean值：" + obj);
        } catch (Throwable e) {
            if (strict) {
                throw e;
            }
        }

        return null;
    }

    public static Number toNumber(Object obj, boolean isFloat, boolean strict) {

        try {
            return isFloat ? Double.parseDouble(obj.toString()) : Long.parseLong(obj.toString());
        } catch (Throwable e) {
            if (strict) {
                throw e;
            }
        }
        return null;
    }

    public static boolean hasFloat(Object... args) {
        if (args == null || args.length == 0) {
            return false;
        }

        for (Object obj : args) {

            if (obj == null) {
                continue;
            } else if (obj instanceof Double || obj instanceof Float) {
                return true;
            } else if ("float".equals(obj.getClass().getName()) || "double".equals(obj.getClass().getName())) {
                return true;
            }
        }


        return false;
    }

//    private static final int OBJECT = 0,
//            NUM_SHORT = 1,
//            NUM_INTEGER = 2,
//            NUM_LONG = 3,
//            NUM_FLOAT = 4,
//            NUM_DOUBLE = 5,
//            BOOLEAN = 6,
//            DATETIME = 7;
//
//    /**
//     * 获取类型代码
//     *
//     * @param clazz
//     * @return
//     */
//    private static int geTypeCode(Class<?> clazz) {
//        switch (clazz.getName()) {
//            case "long":
//            case "java.lang.Long":
//                return NUM_LONG;
//            case "int":
//            case "java.lang.Integer":
//                return NUM_INTEGER;
//            case "double":
//            case "java.lang.Double":
//                return NUM_DOUBLE;
//            case "float":
//            case "java.lang.Float":
//                return NUM_INTEGER;
//            case "short":
//            case "java.lang.Short":
//                return NUM_SHORT;
//            case "boolean":
//            case "java.lang.Boolean":
//                return BOOLEAN;
//            case "mano.DateTime":
//                return DATETIME;
//            default:
//                return 0;
//        }
//    }

//    /**
//     * 转换对象类型
//     *
//     * @param <T>
//     * @param clazz
//     * @param obj
//     * @return
//     */
//    private static <T> T cast(Class<T> clazz, Object obj) {
//        Object result;
//        int code = geTypeCode(clazz);
//        switch (code) {
//            case NUM_DOUBLE:
//            case NUM_FLOAT:
//            case NUM_INTEGER:
//            case NUM_LONG:
//            case NUM_SHORT:
//                result = asNumber(code, toDouble(obj));
//                break;
//            case BOOLEAN:
//                result = Boolean.parseBoolean(obj.toString());
//                break;
//            default:
//                return clazz.cast(obj);
//        }
//        return (T) result;
//    }
//
//    private static Object asNumber(int type, double obj) {
//        Object result;
//
//        switch (type) {
//            case NUM_DOUBLE:
//                return obj;
//            case NUM_FLOAT:
//                return (float) obj;
//            case NUM_INTEGER:
//                return (int) obj;
//            case NUM_LONG:
//                return (long) obj;
//            case NUM_SHORT:
//                return (short) obj;
//            default:
//                return obj;
//        }
//    }
//
//    private static double toDouble(Object obj) {
//        if (obj == null) {
//            throw new IllegalArgumentException("null is not a valid number.");
//        }
//        return Double.parseDouble(obj.toString());
//    }
//
//    private static long toLong(Object obj) {
//        if (obj == null) {
//            throw new IllegalArgumentException("null is not a valid number.");
//        }
//        return Long.parseLong(obj.toString());
//    }
//
//    private static long toInt(Object obj) {
//        if (obj == null) {
//            throw new IllegalArgumentException("null is not a valid number.");
//        }
//        return Integer.parseInt(obj.toString());
//    }


    public static <T> T cast(Object obj, Class<T> resultType, boolean strict) {
        Object result = null;
        Number n;
        Boolean b;
        switch (resultType.getName()) {
            case "long":
                n = toNumber(obj, false, strict);
                result = n == null ? 0 : n.longValue();
                break;
            case "java.lang.Long":
                n = toNumber(obj, false, strict);
                result = n == null ? null : n.longValue();
                break;
            case "int":
                n = toNumber(obj, false, strict);
                result = n == null ? 0 : n.intValue();
                break;
            case "java.lang.Integer":
                n = toNumber(obj, false, strict);
                result = n == null ? null : n.intValue();
                break;
            case "double":
                n = toNumber(obj, true, strict);
                result = n == null ? 0 : n.doubleValue();
                break;
            case "java.lang.Double":
                n = toNumber(obj, true, strict);
                result = n == null ? null : n.doubleValue();
                break;
            case "float":
                n = toNumber(obj, true, strict);
                result = n == null ? 0 : n.floatValue();
                break;
            case "java.lang.Float":
                n = toNumber(obj, true, strict);
                result = n == null ? null : n.floatValue();
                break;
            case "short":
                n = toNumber(obj, true, strict);
                result = n == null ? new Short("0") : n.shortValue();
                break;
            case "java.lang.Short":
                n = toNumber(obj, true, strict);
                result = n == null ? null : n.shortValue();
                break;
            case "boolean":
                b = toBoolean(obj, strict);
                result = b == null ? false : b;
                break;
            case "java.lang.Boolean":
                b = toBoolean(obj, strict);
                result = b == null ? null : b;
                break;
            default:
                try {
                    result = resultType.cast(obj);
                } catch (Throwable e) {
                    if (strict) {
                        throw e;
                    }
                }
        }
        return (T) result;
    }

}
