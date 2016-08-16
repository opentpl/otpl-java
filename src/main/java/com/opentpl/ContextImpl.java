package com.opentpl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Jun Hwong
 */
public class ContextImpl implements Context {


    class ContextScope {
        private final Stack<Object> stack;
        private final Map<String, Object> map;

        ContextScope(Map<String, ?> dataMap, ContextScope parent) {
            if (parent != null) {
                this.map = new HashMap<>(parent.map);
            } else {
                this.map = new HashMap<>();
            }

            if (dataMap != null) {
                dataMap.forEach((key, value) -> {
                    this.map.put(key.toLowerCase().trim(), value);
                });
            }
            this.stack = new Stack<>();
        }

        public Object pop() {

            return stack.isEmpty() ? null : stack.pop();
        }

        public void push(Object value) {
            stack.push(value);
        }

        public Object get(String name) {
            if (name == null) {
                return null;
            }
            return map.getOrDefault(name.trim().toLowerCase(), null);
        }

        public void set(String name, Object value) {
            if (name == null) {
                throw new IllegalArgumentException("name");
            }
            map.put(name.trim().toLowerCase(), value);
        }

        public void destory() {
            stack.clear();
            map.clear();
        }

    }

    private static final Log log = LogFactory.getLog(BufferUtils.class);
    private Stack<ContextScope> scopes = new Stack<>();
    private ContextScope currentScope;
    private PrintStream outstream;
    private Interpreter interpreter;
    private Map<String, Loader> loaders = new HashMap<>();

    public ContextImpl(Interpreter interpreter, Map<String, ?> dataMap, PrintStream outstream) {
        this.interpreter = interpreter;
        this.currentScope = new ContextScope(dataMap, null);
        this.outstream = outstream;
    }


    @Override
    public boolean isStrict() {
        return interpreter.isStrictMode();
    }

    @Override
    public Object pop() {
        return currentScope.pop();
    }

    @Override
    public void push(Object value) {
        currentScope.push(value);
    }

    @Override
    public Object getVariable(String name) {
        return currentScope.get(name);
    }

    @Override
    public void setVariable(String name, Object value) {
        currentScope.set(name, value);
    }

    @Override
    public void print(Object value, boolean escape) {
        if (escape) {
            //TODO: 编码
        }
        outstream.print(value == null ? "" : value);
    }

    @Override
    public void scope() {
        scopes.push(currentScope);
        currentScope = new ContextScope(null, currentScope);
    }

    @Override
    public void unscope() {
        if (scopes.size() == 0) {
            throw new IllegalStateException("scopes size is 0.");
        }
        currentScope.destory();
        currentScope = scopes.pop();
    }

    @Override
    public void exec(Loader loader, int ptr) throws Exception {
        interpreter.exec(this, loader, ptr);
    }

    @Override
    public Loader getLoader(String viewName, String ref) {
        return getLoaderWithILFile(interpreter.getILFile(viewName, ref), ref);
    }

    @Override
    public Loader getLoaderWithILFile(File ifFile, String ref) {

        String key = ifFile.toString() + ref;

        Loader loader = loaders.getOrDefault(key, null);

        if (loader == null && ifFile.exists()) {

            try {
                loader = Loader.open(ifFile);
                //TODO:
            } catch (Exception e) {
                log.debug("Filed to open Loader:", e);
            }

            if (loader != null) {
                loaders.put(key, loader);
            }

        }

        return loader;
    }

    public void destory() {
        loaders.forEach((key, loader) -> {
            loader.destory();
        });
        loaders.clear();

        try {
            outstream.flush();
        } catch (Throwable e) {
        }
        outstream = null;

        try {
            currentScope.destory();
        } catch (Throwable e) {
        }
        currentScope = null;

        scopes.forEach(s -> {
            s.destory();
        });
        scopes.clear();


    }

}
