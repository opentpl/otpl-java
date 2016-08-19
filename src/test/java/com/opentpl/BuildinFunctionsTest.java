package com.opentpl;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun Hwong
 */
public class BuildinFunctionsTest {

    @Test
    public void testFunc() throws InvocationTargetException, IllegalAccessException {

        TemplateFunction method = BuildinFunctions.getFunction("str");

        Assert.assertNotNull(method);

//        System.out.println(method.getParameterTypes()[0]);

        List args = new ArrayList();
        args.add("1");
        args.add(2);
        args.add("3");


        Assert.assertEquals(method.call(args), "123");

    }

}