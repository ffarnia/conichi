package com.conichi.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Fazel on 11/3/2019.
 */
public class MessageException {
    private Integer code;
    private String info;
    private String type;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
