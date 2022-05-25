package com.sunshine.utils.pwd;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: JwtHelper
 * @Description: token工具类
 * @author: whq
 * @date: 2018/8/17 9:59
 * @version: V 2.0.0
 * @since: (jdk_1.8)
 */
public class JwtUtils {


    /**
     * 获取token中的参数
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token, String key) {
        if ("".equals(token)) {
            return null;
        }

        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 生成token
     *
     * @param userId
     * @return
     */
    public static String createToken(final Map<String, Object> clams, String key, Duration duration) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        final JwtBuilder builder = Jwts.builder();
        clams.entrySet().forEach(
                entry -> builder.claim(entry.getKey(), entry.getValue())
        );
        if (duration != null) {
            builder.setExpiration(DateTime.now().plus(duration).toDate());// 设置超时时间
        }

        builder.signWith(signatureAlgorithm, signingKey);

        //生成JWT
        return builder.compact();
    }

    public static String createToken(String uid, String key, Duration duration) {
        final Map<String, Object> clams = new HashMap<>();
        clams.put("uid", uid);
        return createToken(clams, key, duration);
    }


    public static void main(String[] args) {

        String token = JwtUtils.createToken("123", "admin", Duration.standardHours(2));
        System.out.println(token);

//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiemhhbmdzYW4iLCJhZ2UiOjIzLCJzZXgiOiLnlLciLCJleHAiOjE2MDY3MjYyOTB9.3yrt1Njzy7FTq56oz6u4W40Jv9msh_77tubN10TLTYI";

        Claims claims = JwtUtils.parseToken(token, "admin");

        System.out.println(claims);

    }


}