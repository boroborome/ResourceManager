package com.happy3w.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.happy3w.common.util.ErrorCode;

/**
 * Created by ysgao on 4/7/16.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class WebCommonResult {
    private int code = ErrorCode.SUCCESS;
    private Object message;
    private Object data;

    public WebCommonResult() {
    }

    public WebCommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WebCommonResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
