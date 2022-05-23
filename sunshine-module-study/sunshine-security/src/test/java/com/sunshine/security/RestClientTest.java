package com.sunshine.security;

import redis.clients.jedis.Jedis;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/21 16:58
 **/
public class RestClientTest {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("127.0.0.1", 6379);
// 2.jedis执行set操作
        jedis.set("hello", "world");
//3.jedis执行get操作,value="world"
        String value = jedis.get("hello");
        System.out.println(value);
    }
}
