package com.opentpl.opil;

import com.opentpl.Context;
import com.opentpl.Loader;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Jun Hwong
 */
public class CastToIterator extends Opcode {


    class ObjectIterator {

        Context context;
        Iterator iterator;
        boolean isMap;
        Object current;
        int index = -1;
        boolean isNext;

        public ObjectIterator(Context context, Object obj) {
            this.context = context;
            cast(obj);
        }

        private void cast(Object obj) {
            if (obj == null) {

            } else if (obj instanceof Map) {
                isMap = true;
                iterator = ((Map) obj).entrySet().iterator();
            } else if (obj instanceof Iterator) {
                iterator = (Iterator) obj;
            } else if (obj instanceof Iterable) {
                iterator = ((Iterable) obj).iterator();
            } else if (obj.getClass().isArray()) {

                final int len = Array.getLength(obj);
                iterator = new Iterator() {
                    int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < len;
                    }

                    @Override
                    public Object next() {
                        return Array.get(obj, i++);
                    }
                };
            } else if (!obj.getClass().isPrimitive()) {
                throw new UnsupportedOperationException("待实现");//TODO:
            }

            if (iterator == null) {
                iterator = new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public Object next() {
                        return null;
                    }
                };
            }
            if (iterator.hasNext()) {
                next();
                isNext = true;
            }
        }


        public boolean hasNext() {

            return isNext;
        }

        public Object next() {
            index++;
            try {
                //采用这种方式来解决，先执行的一次循环
                current = iterator.next();
                isNext = true;
            } catch (Throwable e) {
                isNext = iterator.hasNext();
            }
            return current;
        }

        public void setVariables(String keyName, String valueName) {
//            System.out.println("set>"+keyName+","+valueName+"====="+index);
            if (keyName != null && keyName.trim().length() > 0) {
                context.setVariable(keyName, isMap ? getEntry(current).getKey() : index);
            }
            if (valueName != null && valueName.trim().length() > 0) {
                context.setVariable(valueName, isMap ? getEntry(current).getValue() : current);
            }
        }

        Map.Entry getEntry(Object obj) {
            if (obj == null || !(obj instanceof Map.Entry)) {
                new Map.Entry() {
                    @Override
                    public Object getKey() {
                        return null;
                    }

                    @Override
                    public Object getValue() {
                        return null;
                    }

                    @Override
                    public Object setValue(Object value) {
                        return null;
                    }
                };
            }
            return (Map.Entry) obj;
        }

    }


    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0F;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
    }

    @Override
    public int execute(Context context) throws Exception {
        context.push(new ObjectIterator(context, context.pop()));
        return ptr() + 1;
    }
}
