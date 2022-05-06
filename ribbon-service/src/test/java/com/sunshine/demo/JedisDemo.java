package com.sunshine.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/5/6 19:30
 **/
public class JedisDemo {
    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);

//        Set<String> sentinels = new HashSet<String>(Arrays.asList("192.168.56.100:6379", "192.168.56.100:63","192.168.56.100:6381"));


        JedisPool pool = new JedisPool("192.168.56.100",6379);
        Jedis jedis = pool.getResource();

        jedis.set("SentinelKey","SentinelValue2");
        System.out.println(jedis.get("SentinelKey"));
    }
}
