package com.opentpl.opil;

import com.opentpl.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jun Hwong
 */
public class Call extends Opcode {

    /**
     * 操作码类型代码.
     */
    public static final byte CODE = 0x07;

    private int parameterCount;

    @Override
    protected void load(int ptr, Loader loader) throws Exception {
        setPtr(ptr);
        setLineNumber(loader.readInt());
        parameterCount = loader.readInt();
    }

    @Override
    public int execute(Context context) throws Exception {

        Object instance = null;
        Object methodObj = context.pop();
        Method method = null;

        if (methodObj instanceof String) {
            method = BuildinFunctions.methodMap.getOrDefault(methodObj.toString(), null);

            if (method == null) {

                Object obj = context.getVariable(methodObj.toString());
                if (obj != null && obj instanceof Method) {
                    method = (Method) obj;
                } else if (obj != null && obj instanceof Field) {
                    Field field = (Field) obj;
                    context.push(field.get(instance));
                    return ptr() + 1;
                }
            }
        } else if (methodObj instanceof Method) {
            method = (Method) methodObj;
            instance = context.pop();
        } else if (methodObj instanceof Field) {
            Field field = (Field) methodObj;
            instance = context.pop();
            context.push(field.get(instance));
            return ptr() + 1;
        }

        if (method == null) {
            throw new OtplRuntimeException("函数未找到：" + methodObj + "，line：" + getLineNumber() + ", view:" + getLoader().getViewName());
        }

        //获取参数
        List<Object> parameters = new ArrayList<>(method.getParameterCount());
        for (int i = 0; i < parameterCount; i++) {
            parameters.add(context.pop());
        }

        Collections.reverse(parameters);


        //补齐参数
        if (parameterCount < method.getParameterCount()) {
            for (int i = 0; i < method.getParameterCount() - parameterCount; i++) {
                parameters.add(null);
            }
        }

        //类型转换
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < parameters.size(); i++) {
            parameters.set(i, Convert.cast(parameters.get(i), types[i], false));
        }


        Object result = method.invoke(instance, parameters.toArray());


        context.push(result);

        return ptr() + 1;
    }
}
