package org.opentpl;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jun Hwong
 */
public class BuildinFunctions {
    private static final Map<String, TemplateFunction> functionMap = new HashMap<>();

    static {

        //当前系统时间
        register(new TemplateFunction() {
            @Override
            public String functionName() {
                return "now";
            }

            @Override
            public Object call(List parameters) {
                return System.currentTimeMillis() / 1000;
            }
        });

        register(new TemplateFunction() {
            @Override
            public String functionName() {
                return "str";
            }

            @Override
            public Object call(List parameters) {
                String result = "";
                if (parameters == null) {
                    return result;
                }
                for (Object obj : parameters) {
                    if (obj != null) {
                        result += obj;
                    }
                }
                return result;
            }
        });

        register(new TemplateFunction() {
            @Override
            public String functionName() {
                return "range";
            }

            @Override
            public Object call(List parameters) {

                Long start = null, stop = null, step = null;

                if (parameters == null || parameters.size() == 0) {
                    throw new IllegalArgumentException("parameters");
                } else if (parameters.size() >= 3) {
                    start = Convert.cast(parameters.get(0), Long.class, false);
                    stop = Convert.cast(parameters.get(1), Long.class, false);
                    step = Convert.cast(parameters.get(2), Long.class, false);
                } else if (parameters.size() == 2) {
                    start = Convert.cast(parameters.get(0), Long.class, false);
                    stop = Convert.cast(parameters.get(1), Long.class, false);

                } else if (parameters.size() == 1) {
                    start = Convert.cast(parameters.get(0), Long.class, false);
                }

                if (stop == null && step == null) {
                    stop = start;
                    start = 0L;
                    step = 1L;
                } else if (step == null) {
                    step = 1L;
                }

                return new RangeIterator(start, stop, step);
            }
        });

        register(new TemplateFunction() {
            @Override
            public String functionName() {
                return "len";
            }

            @Override
            public Object call(List parameters) {

                if (parameters == null || parameters.size() == 0) {
                    return -1;
                }
                Object obj = parameters.get(0);
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
        });

        //日期格式化
        register(new TemplateFunction() {
            @Override
            public String functionName() {
                return "time";
            }

            @Override
            public Object call(List parameters) {
                String result = "";
                if (parameters == null || parameters.size() < 2) {
                    return result;
                }
                long time = (long) parameters.get(0);
                Date date = new Date(time * 1000);

                SimpleDateFormat format = new SimpleDateFormat(parameters.get(1).toString());

                result = format.format(date);

                return result;
            }
        });


    }

    public static void register(TemplateFunction function) {
        functionMap.put(function.functionName(), function);
    }

    public static TemplateFunction getFunction(String name) {
        return functionMap.getOrDefault(name, null);
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


}
