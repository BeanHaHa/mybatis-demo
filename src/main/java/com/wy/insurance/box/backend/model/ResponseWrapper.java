package com.wy.insurance.box.backend.model;

import java.io.Serializable;

/**
 * 接口返回结果
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class ResponseWrapper implements Serializable {

    private String msg;
    private Integer code;
    private Object data;

    public ResponseWrapper() {

    }

    public ResponseWrapper(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
