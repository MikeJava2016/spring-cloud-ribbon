package com.sunshine.security;

import com.sunshine.utils.pwd.JwtTokenUtils;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/24 21:08
 **/
public class JwtTest {
    public static void main(String[] args) {
        String token = JwtTokenUtils.createToken("1", 60 * 60 * 24, "123456");
        String value = JwtTokenUtils.parseToken(token, "123456");
        System.out.println(value);
    }
}
