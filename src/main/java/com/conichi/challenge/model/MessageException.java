package com.conichi.challenge.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author Fazel Farnia
 */
public class MessageException {
    private Integer code;
    private String info;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
