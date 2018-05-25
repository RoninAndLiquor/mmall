package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Slf4j
public class ShardedRedisPoolUtil {
    /**
     * 设置key的有效期
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} second:{} error",key,exTime,e);
            RedisShardedPool.returnBrokenResources(jedis);
        }
        RedisShardedPool.returnResources(jedis);
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
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} exTime{} error",key,value,exTime,e);
            RedisShardedPool.returnBrokenResources(jedis);
        }
        RedisShardedPool.returnResources(jedis);
        return result;
    }

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResources(jedis);
        }
        RedisShardedPool.returnResources(jedis);
        return result;
    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisShardedPool.returnBrokenResources(jedis);
        }
        RedisShardedPool.returnResources(jedis);
        return result;
    }

    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisShardedPool.returnBrokenResources(jedis);
        }
        RedisShardedPool.returnResources(jedis);
        return result;
    }

    public static void main(String[] args) {
        String set = ShardedRedisPoolUtil.set("high", "giiii");
        System.out.println(set);
        String s = ShardedRedisPoolUtil.setEx("exhigh", 60, "exhigh");
        System.out.println(s);
    }

}
