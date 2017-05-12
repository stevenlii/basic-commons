package com.paymoon.basic.commons.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
	private static JedisPool pool;
	private static Logger logger = LogManager.getLogger();
    static {
    	logger.debug("start to read redis configuration..");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(MyProp.getVariable("redis.maxTotal", "500")));
        config.setMaxIdle(Integer.valueOf(MyProp.getVariable("redis.maxIdle", "200")));
        config.setMaxWaitMillis(5000L);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        
        logger.debug("read success,start to create redis pool");
        pool = new JedisPool(config, 
        		MyProp.getVariable("redis.host", "localhost"), 
        		Integer.valueOf(MyProp.getVariable("redis.port")), 
        		2000, MyProp.getVariable("redis.password", "localhost"));
        logger.debug("create redis pool success.");
    }

    private RedisUtil() {}
    
    /**
     * dbIndex == null?MyProp.redis.db.index:dbIndex
     * @param dbIndex
     * @return
     */
    public static Jedis getJedis(Integer dbIndex) {
    	if (dbIndex == null) 
			dbIndex = Integer.valueOf(MyProp.getVariable("redis.db.index", "10"));
    	Jedis jedis = getPool().getResource();
    				jedis.select(dbIndex);
         return jedis;
    }

	/**
     * default is 0
     * @return
     */
    public static Jedis getJedis() {
    	return getJedis(0);
    	}
    public static JedisPool getPool() {
        return pool;
    }
    public static Jedis closeJedis(Jedis jedis) {
    	if(jedis != null) {
    		jedis.close();
    		jedis = null;
    	}
    	return jedis;
    }
    public static void destroyPool(Jedis jedis) {
        getPool().destroy();
    }
    public static void main(String[] args) {
    	Jedis jedis = RedisUtil.getJedis();
    	jedis.set("name", "zhangsan");
		System.out.println(jedis.get("name"));
		jedis = RedisUtil.closeJedis(jedis);
		jedis = RedisUtil.closeJedis(jedis);
	}
    
   
}
