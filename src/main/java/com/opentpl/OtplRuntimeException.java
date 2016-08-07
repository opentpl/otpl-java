package com.opentpl;

/**
 * @author Jun Hwong
 */
public class OtplRuntimeException extends Exception {
    public OtplRuntimeException(String message) {
        super(message);
    }

    public OtplRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtplRuntimeException(Throwable cause) {
        super(cause);
    }
}
