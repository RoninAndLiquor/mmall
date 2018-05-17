package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {
    /**
     * 设置key的有效期
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} second:{} error",key,exTime,e);
            RedisPool.returnBrokenResources(jedis);
        }
        RedisPool.returnResources(jedis);
        return result;
    }

    /**
     * 添加key-value 并设置有效期
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static String setEx(String key,int exTime,String value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} exTime{} error",key,value,exTime,e);
            RedisPool.returnBrokenResources(jedis);
        }
        RedisPool.returnResources(jedis);
        return result;
    }

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResources(jedis);
        }
        RedisPool.returnResources(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResources(jedis);
        }
        RedisPool.returnResources(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResources(jedis);
        }
        RedisPool.returnResources(jedis);
        return result;
    }

    public static void main(String[] args) {
        String set = RedisPoolUtil.set("high", "giiii");
        System.out.println(set);
        String s = RedisPoolUtil.setEx("exhigh", 60, "exhigh");
        System.out.println(s);
    }

}
