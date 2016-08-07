package com.opentpl;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jun Hwong
 */
public class BuildinFunctionsTest {

    @Test
    public void testFunc() throws InvocationTargetException, IllegalAccessException {

        Method method = BuildinFunctions.methodMap.get("str");

        Assert.assertNotNull(method);

        System.out.println(method.getParameterTypes()[0]);

        Object[] args = new Object[]{"1", 2, 3};


        Assert.assertEquals(method.invoke(null, new Object[]{args}), "123");

    }

}