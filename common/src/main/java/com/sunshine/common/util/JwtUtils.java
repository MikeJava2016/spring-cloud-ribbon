package com.sunshine.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/9 20:00
 **/
public class JwtUtils {

    private static final String SECRET_KEY = "abcd1234";

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key key = new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
        return key;
    }

    /**
     * 生成jwt token
     *
     * @param payLoad
     * @return
     */
    public static String getToken(Map<String, Object> payLoad) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = Jwts.builder().setPayload(objectMapper.writeValueAsString(payLoad)).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
            return token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public static void main(String[] args) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("id", "1234");
        param.put("username", "huzhanglin");
        param.put("exp", DateTime.now().plusHours(1).toDate().getTime()/1000);
        String token = getToken(param);
        System.out.println(token);

        Claims claims = parseToken(token);
        claims.setExpiration( DateTime.now().plusHours(2).toDate());
        System.out.println(claims.get("id"));

    }
}
