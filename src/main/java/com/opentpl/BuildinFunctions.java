package com.opentpl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Jun Hwong
 */
public class BuildinFunctions {
    public static final Map<String, Method> methodMap = new HashMap<>();

    static {
        for (Method method : BuildinFunctions.class.getDeclaredMethods()) {
            method.setAccessible(true);
            methodMap.put(method.getName(), method);
        }

    }

    public static String str(Object[] args) {
        String result = "";
        if (args == null) {
            return result;
        }
        for (Object obj : args) {
            if (obj != null) {
                result += obj;
            }
        }
        return result;
    }

    public static Iterator<Long> range(Long start, Long stop, Long step) {

        if (stop == null && step == null) {
            stop = start;
            start = 0L;
            step = 1L;
        } else if (step == null) {
            step = 1L;
        }

        return new RangeIterator(start, stop, step);

    }

    static class RangeIterator implements Iterator<Long> {
        public RangeIterator(Long _start, Long _stop, Long _step) {
            this._start = _start;
            this._stop = _stop;
            this._step = _step;
        }

        long _start;
        long _stop;
        long _step;

        @Override
        public boolean hasNext() {
            return _start < _stop;
        }

        @Override
        public Long next() {
            if (!hasNext()) {
                throw new NoSuchElementException("已经迭代完成");
            }
            _start += _step;
            return _start;
        }
    }

    public static int len(Object obj) {
        if (obj == null) {
            return -1;
        } else if (obj instanceof Map) {
            return ((Map) obj).size();
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size();
        } else if (obj instanceof String || "string".equals(obj.getClass().getName())) {
            return obj.toString().length();
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }


        return 0;
    }


}
