package org.opentpl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun Hwong
 */
public class InterpreterTest {

    public static String fn() {
        return "call fn";
    }

    @Test
    public void testRender() throws Exception {
        Interpreter interpreter = new Interpreter("", "F:\\workspace\\exam-web\\bin\\otil");
        interpreter.setDebug(true);
        interpreter.setStrictMode(true);

        Map<String, Object> data = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("current", true);
        map.put("name", "otpl");
        map.put("url", "http://opentpl.github.io");

        data.put("items", new Object[]{map,
                new Item(false, "张三", "url/zs"),
                new Item(false, "李四", "url/ls")
        });
        data.put("model", new Item(false, "李四", "url/ls"));
        data.put("array", new Object[]{"bbbb"});
        data.put("fn", InterpreterTest.class.getDeclaredMethod("fn"));
        //Context context = interpreter.render(data, "index", System.out);//exam/paper
        System.out.println("=============destory");
        //context.destory();

//        Thread.sleep(1000 * 60 * 60);

    }

    class Item {
        public Item(boolean current, String name, String url) {
            this.current = current;
            this.name = name;
            this.url = url;
        }

        boolean current;
        String name;
        String url;

        String getName() {
            return "hello " + name;
        }

        public String fn() {
            return "call fn";
        }


    }

}