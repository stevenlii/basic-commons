package com.paymoon.basic.commons.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;

public class RedisUtilSimple {
	private static Logger logger = LogManager.getLogger();

	public static String get(final String key) {
		logger.debug("query  from Redis, key is "+ key );
		if (StringUtils.isBlank(key))
			return null;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis();
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			logger.debug("failed to fetch from redis cause by Exception,  key is "+ key+", Exception is:"+e.getMessage() );
		}finally {
			RedisUtil.closeJedis(jedis);
		}
		
		return null;
	}
	public static String get(final String key,final int dbindex) {
		logger.debug("query  from Redis, key is "+ key );
		if (StringUtils.isBlank(key))
			return null;
		Jedis jedis = null;
		try {
			jedis = RedisUtil.getJedis(dbindex);
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			logger.debug("failed to fetch from redis cause by Exception,  key is "+ key+", Exception is:"+e.getMessage() );
		}finally {
			RedisUtil.closeJedis(jedis);
		}
		
		return null;
	}
	public static String set(final String key,final String value) {
		logger.debug(" Redis save, key is "+ key );
		if (StringUtils.isBlank(key))
			return null;
		Jedis jedis = null;
		String oldV = null;
		try {
			jedis = RedisUtil.getJedis();
			oldV = jedis.get(key);
			jedis.set(key, value);
		} catch (Exception e) {
			logger.debug("failed to set to redis cause by Exception,  key is "+ key+", Exception is:"+e.getMessage() );
		}finally {
			RedisUtil.closeJedis(jedis);
		}
		
		return oldV;
	}
	public static String set(final String key,final String value,final int dbindex) {
		logger.debug(" Redis save, key is "+ key );
		if (StringUtils.isBlank(key))
			return null;
		Jedis jedis = null;
		String oldV = null;
		try {
			jedis = RedisUtil.getJedis(dbindex);
			oldV = jedis.get(key);
			jedis.set(key, value);
		} catch (Exception e) {
			logger.debug("failed to set to redis cause by Exception,  key is "+ key+", Exception is:"+e.getMessage() );
		}finally {
			RedisUtil.closeJedis(jedis);
		}
		
		return oldV;
	}
	public static String del(final String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Jedis jedis = RedisUtil.getJedis();
		String oldV = jedis.get(key);
		if (StringUtils.isNotBlank(oldV)) {
			jedis.del(key);
		}
		RedisUtil.closeJedis(jedis);
		return oldV;
	}
	public static String del(final String key,final int dbindex) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Jedis jedis = RedisUtil.getJedis(dbindex);
		String oldV = jedis.get(key);
		if (StringUtils.isNotBlank(oldV)) {
			jedis.del(key);
		}
		RedisUtil.closeJedis(jedis);
		return oldV;
	}
	public static void main(String[] args) {
		String res = get("redis_acl_token_e6d7db37-b2c3-c722-591f-a434b3b6d8e4");
		Jedis jedis = RedisUtil.getJedis();
		jedis.flushAll();
		jedis = RedisUtil.closeJedis(jedis);
		System.out.println(res);
	}
}