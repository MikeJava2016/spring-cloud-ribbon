package com.sunshine.common.ao;

import java.io.Serializable;

public abstract class BaseAo implements Serializable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public  abstract void  validate();
}
