package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    //Jedis 连接池
    private static JedisPool pool;
    private static String ip = PropertiesUtil.getProperty("redis.ip");
    private static Integer port = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    //最大空闲
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));
    //最小空闲
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idel","2"));
    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true ，则取出的jedis实例一定是可用的
    private static Boolean testOnBorrow = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.borrow","true")) ;
    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true ，则放回jedisPool中的jedis实例一定是可用的
    private static Boolean testOnReturn = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.return","true")) ;

    public static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候是否阻塞 false会抛出异常  true阻塞直到超时 ， 默认为true
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config,ip,port,1000*2);
    }
    static{
        initPool();
    }
    public static Jedis getJedis(){
        return pool.getResource();
    }
    public static void returnResources(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void returnBrokenResources(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("aa","aa");
        returnResources(jedis);
        pool.destroy();

    }
}
