package com.opentpl;

import java.util.List;

/**
 * 表示一个模板函数
 *
 * @author Jun Hwong
 */
public interface TemplateFunction<TRsult extends Object> {
    /**
     * 函数名称
     *
     * @return
     */
    String functionName();

    /**
     * 参数
     *
     * @param parameters
     * @return
     */
    TRsult call(List<?> parameters);
}
