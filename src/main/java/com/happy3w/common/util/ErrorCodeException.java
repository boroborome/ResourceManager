package com.happy3w.common.util;

/**
 * Created by ysgao on 4/11/16.
 */
public class ErrorCodeException extends Exception implements ICodeMessage {
    private int errorCode;
    private Object[] params;
    private String localedMessage;

    public ErrorCodeException(int errorCode, String localedMessage, Throwable cause) {
        super(localedMessage, cause);
        this.errorCode = errorCode;
        this.localedMessage = localedMessage;
    }

    public ErrorCodeException(int errorCode, Object[] params, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.params = params;
    }

    public ErrorCodeException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public int getCode() {
        return errorCode;
    }

    @Override
    public String getLocaledMessage() {
        return localedMessage;
    }
}
