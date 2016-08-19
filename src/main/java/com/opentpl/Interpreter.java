package com.opentpl;

import com.opentpl.opil.Opcode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
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

    private static final Log log = LogFactory.getLog(Interpreter.class);
    private String viewPath;
    private String ilPath;
    private String[] allowedSuffix = new String[]{".html", ".otpl", ".otpl.html"};
    private boolean debug = false;
    private boolean strictMode = false;

    public Interpreter(String viewPath, String ilPath) {
        this.viewPath = viewPath;
        this.ilPath = ilPath;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
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

        if (viewName.startsWith("/")) {
            base = "/";
        }

        //TODO:针对不同平台的处理
        viewName = Paths.get("X:\\", base, viewName).normalize().toString()
                .substring(2)
                .replace('\\', '/')
                .toLowerCase();

        return viewName;
    }

    public File getILFile(String viewName, String ref) {
        viewName = canonicalViewName(viewName, ref);
        return new File(getIlPath(), BufferUtils.md5(viewName) + ".otil");
    }

    public Context render(Map<String, ?> data, File ifFile, OutputStream outputStream) throws Exception {

        Context context = new ContextImpl(this, data, new PrintStream(outputStream, true, "utf8"));

        Loader loader = context.getLoaderWithILFile(ifFile, "");

        if (loader == null) {
            //TODO: 报错或编译
            context.print("Failed to load view", false);
        } else {
            try {
                exec(context, loader, loader.getStartPtr());
            } catch (Throwable e) {
                context.print("Failed to render view：" + e.getMessage(), false);
                log.debug("Failed to render view：", e);
            }
        }

        return context;
    }


    public Context render(Map<String, ?> data, String viewName, OutputStream outputStream) throws Exception {
        File file = getILFile(viewName, "");
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("Not found view：" + viewName);
        }

        return render(data, file, outputStream);
    }


    public void exec(Context context, Loader loader, int startPtr) throws Exception {

        Opcode code;
        while (startPtr > 0) {

            code = loader.load(startPtr);

            if (code == null) {
                throw new IllegalArgumentException("ptr无效：" + startPtr);//TODO:使用上一个行号来推导出当前错误位置
            }

            try {
                startPtr = code.execute(context);
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage() + "，line：" + code.getLineNumber() + ", view:" + loader.getViewName(), e);
            }

        }

    }

}
