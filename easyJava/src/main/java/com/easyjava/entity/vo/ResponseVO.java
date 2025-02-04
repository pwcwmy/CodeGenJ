package com.easyjava.entity.vo;

public class ResponseVO<T> {
    /**
     * code：表示HTTP状态码或自定义业务状态码。
     */
    private Integer code;
    /**
     * status：状态类型（如“success”、“error”）
     */
    private String status;
    /**
     * message：给开发者的详细错误信息或成功提示。
     */
    private String message;
    /**
     * data：实际返回的数据内容。
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
