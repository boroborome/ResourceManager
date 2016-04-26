package com.happy3w.common.util;

/**
 * Created by ysgao on 4/25/16.
 */
public class CodeMessage implements ICodeMessage {
    private int code;
    private Object[] params;
    private String localedMessage;

    public CodeMessage() {
    }

    public CodeMessage(int code, Object[] params) {
        this.code = code;
        this.params = params;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String getLocaledMessage() {
        return localedMessage;
    }

    public void setLocaledMessage(String localedMessage) {
        this.localedMessage = localedMessage;
    }
}
