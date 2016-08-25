package org.opentpl;

import java.io.File;

/**
 * @author Jun Hwong
 */
public interface Context {

    boolean isStrict();

    Object pop();

    void push(Object value);

    Object getVariable(String name);

    void setVariable(String name, Object value);

    void print(Object value, boolean escape);

    void scope();

    void unscope();

    void exec(Loader loader, int ptr) throws Exception;

    Loader getLoader(String src, String ref);

    Loader getLoaderWithILFile(File ifFile, String ref);

    void destory();
}
