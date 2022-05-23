package com.sunshine.entity;

import annotation.JsonEncrypt;
import com.sunshine.common.ao.BaseAo;

import java.io.Serializable;
public class User extends BaseAo {

    private Long id;

    private String username;
    @JsonEncrypt(beginIdx = 1, endIdx = 2)
    private String password;

    public User(Long id) {
        this.id = id;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public void validate() {

    }
}