package com.gupaoedu.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/16 20:11
 **/
public class PasswordEncoderTest {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String java = passwordEncoder.encode("java");
        System.out.println(java);
        PasswordEncoder passwordEncoderMd5 = new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5");
        System.out.println(passwordEncoderMd5.encode(java));
    }

    @Test
    void bCryptPasswordTest(){
        PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
        String rawPassword = "123456";  //原始密码
        String encodedPassword = passwordEncoder.encode(rawPassword); //加密后的密码

        System.out.println("原始密码" + rawPassword);
        System.out.println("加密之后的hash密码:" + encodedPassword);

        System.out.println(rawPassword + "是否匹配" + encodedPassword + ":"   //密码校验：true
                + passwordEncoder.matches(rawPassword, encodedPassword));

        System.out.println("654321是否匹配" + encodedPassword + ":"   //定义一个错误的密码进行校验:false
                + passwordEncoder.matches("654321", encodedPassword));
    }


}
