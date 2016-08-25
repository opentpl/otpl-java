package org.opentpl.opil;

import org.opentpl.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Jun Hwong
 */
public class LoadMember extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x0A;

    private int parameterCount;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());

        parameterCount = loader.readInt();

    }

    @TODO("自定义的类型c#中的索引器。 除此之外，将只使用parameters中的第一个参数，其它将忽略")
    @Override
    public int execute(Context context) throws Exception {
        Object instance = context.pop();
        List<Object> parameters = new ArrayList<>(parameterCount);
        for (int i = 0; i < parameterCount; i++) {
            parameters.add(context.pop());
        }

        Collections.reverse(parameters);

        if (instance == null) {
            if (context.isStrict()) {
                throw new OtplRuntimeException("不能在null值中获取对象成员");
            }
            context.push(null);
            return ptr() + 1;
        }

        if (parameters.size() == 0) {
            if (context.isStrict()) {
                throw new OtplRuntimeException("丢失参数(成员?)");
            }
            context.push(null);
            return ptr() + 1;
        }

        if (instance.getClass().isPrimitive()) {
            if (context.isStrict()) {
                throw new OtplRuntimeException("原生对象没有成员");
            }
            context.push(null);
            return ptr() + 1;
        }

        //处理集合
        if (instance instanceof Collection) {
            Collection coll = (Collection) instance;
            instance = coll.toArray();//直接让下个环节处理
        }

        //处理数组
        if (instance.getClass().isArray()) {
            Number n = Convert.toNumber(parameters.remove(0), false, context.isStrict());
            if (n != null) {
                int len = Array.getLength(instance);
                int index = n.intValue();
                if (index >= len) {
                    if (context.isStrict()) {
                        throw new OtplRuntimeException("超出数组长度：" + index + "/" + len);
                    }
                    context.push(null);
                } else {
                    context.push(Array.get(instance, index));
                }
            } else {
                context.push(null);
            }
            return ptr() + 1;
        }

        //处理map
        if (instance instanceof Map) {
            Map map = (Map) instance;
            context.push(map.getOrDefault(parameters.remove(0), null));
            return ptr() + 1;
        }

        String name = "get";

        Method[] methods = instance.getClass().getDeclaredMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {

            if (methods[i].getName().equalsIgnoreCase(name) && methods[i].getParameterCount() == parameters.size()) {
                method = methods[i];
                method.setAccessible(true);
                break;
            }
        }

        //处理索引器
        if (method != null) {

            //类型转换
            Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < parameters.size(); i++) {
                parameters.set(i, Convert.cast(parameters.get(i), types[i], false));
            }
            context.push(method.invoke(instance, parameters.toArray()));
            return ptr() + 1;
        }

        //处理getter
        name = parameters.get(0).toString();
        for (int i = 0; i < methods.length; i++) {

            if ((methods[i].getName().equalsIgnoreCase("get" + name) || methods[i].getName().equalsIgnoreCase("is" + name)) && methods[i].getParameterCount() == 0) {
                method = methods[i];
                method.setAccessible(true);

                context.push(method.invoke(instance, new Object[0]));
                return ptr() + 1;
            }
        }


        name = parameters.remove(0).toString();

        //处理字段
        Field[] fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            if (fields[i].getName().equalsIgnoreCase(name)) {
                Field field = fields[i];
                field.setAccessible(true);
                context.push(field.get(instance));
                return ptr() + 1;
            }
        }


        //处理方法
        for (int i = 0; i < methods.length; i++) {

            if (methods[i].getName().equalsIgnoreCase(name)) {// && methods[i].getParameterCount()==parameters.size()
                method = methods[i];
                method.setAccessible(true);

//                //类型转换
//                Class<?>[] types=method.getParameterTypes();
//                for (int j = 0; j < parameters.size(); j++) {
//                    parameters.set(j, Convert.cast(parameters.get(j),types[j],false));
//                }
//
//                context.push(method.invoke(instance,parameters.toArray())); //TODO:是否执行方法？
                context.push(instance);//传入对象实例，方便调用
                context.push(method);
                return ptr() + 1;
            }
        }

        context.push(null);

        return ptr() + 1;
    }
}
