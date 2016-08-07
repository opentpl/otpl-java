package com.opentpl;

import com.opentpl.opil.Opcode;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Map;

/**
 * OTPL解释器
 *
 * @author Jun Hwong
 */
public class Interpreter {

    private String viewPath;
    private String ilPath;
    private String[] allowedSuffix = new String[]{".html", ".otpl", ".otpl.html"};

    public Interpreter(String viewPath, String ilPath) {
        this.viewPath = viewPath;
        this.ilPath = ilPath;
    }

    public String getViewPath() {
        return viewPath;
    }

    public String getIlPath() {
        return ilPath;
    }

    public String canonicalViewName(String viewName, String base) {

        viewName = viewName.trim();

        if (base == null) {
            base = "";
        } else {
            base = base.trim().replace('\\', '/');
        }

        int index = base.lastIndexOf('/');
        if (index > -1) {
            base = base.substring(0, index);
        }
        if (!base.startsWith("/")) {
            base = "/" + base;
        }

        //TODO:针对不同平台的处理
        viewName = Paths.get("X:\\", base, viewName).normalize().toString()
                .substring(2)
                .replace('\\', '/')
                .toLowerCase();

        return viewName;
    }


    public Context render(Map<String, ?> data, String viewName, OutputStream outputStream) throws Exception {

        Context context = new ContextImpl(this, data, new PrintStream(outputStream, true, "utf8"));

        Loader loader = context.getLoader(viewName, "");

        if (loader == null) {
            //TODO: 报错或编译
            context.print("未找到视图：" + viewName, false);
        }

        if (loader != null) {
            exec(context, loader, loader.getStartPtr());
        }

        return context;
    }


    public void exec(Context context, Loader loader, int startPtr) throws Exception {

        Opcode code;
        while (startPtr > 0) {

            code = loader.load(startPtr);

            if (code == null) {
                throw new IllegalArgumentException("ptr无效：" + startPtr);
            }

            startPtr = code.execute(context);

        }

    }

}
