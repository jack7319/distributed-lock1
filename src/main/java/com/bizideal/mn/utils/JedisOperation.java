package com.bizideal.mn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author : liulq
 * @date: 创建时间: 2018/3/2 13:47
 * @version: 1.0
 * @Description: jedis操作redis工具类
 */
@Component
public class JedisOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisOperation.class);

    @Autowired
    private JedisPool jedisPool;

    public String set(String key, String value) {
        String result = null;
        Jedis jedisClient = jedisPool.getResource();
        if (jedisClient == null) {
            return result;
        }
        try {
            result = jedisClient.set(key, value);
        } catch (Exception e) {
            LOGGER.error("redis set error ", e);
        } finally {
            jedisClient.close();
        }
        return result;
    }

    public String get(String key) {
        String result = null;
        Jedis jedisClient = jedisPool.getResource();
        if (jedisClient == null) {
            return result;
        }
        try {
            result = jedisClient.get(key);
        } catch (Exception e) {
            LOGGER.error("redis get error ", e);
        } finally {
            jedisClient.close();
        }
        return result;
    }

    public long del(String key) {
        long result = 0;
        Jedis jedisClient = jedisPool.getResource();
        if (jedisClient == null) {
            return result;
        }
        try {
            result = jedisClient.del(key);
        } catch (Exception e) {
            LOGGER.error("redis del error ", e);
        } finally {
            jedisClient.close();
        }
        return result;
    }

    public long expire(String key, int seconds) {
        long result = 0;
        Jedis jedisClient = jedisPool.getResource();
        if (jedisClient == null) {
            return result;
        }
        try {
            result = jedisClient.expire(key, seconds);
        } catch (Exception e) {
            LOGGER.error("redis expire error ", e);
        } finally {
            jedisClient.close();
        }
        return result;
    }

    /**
     * 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖。
     *
     * @param key
     * @param value
     * @param nxxx  nxxx的值只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
     * @param expx  expx的值只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
     * @param time  过期时间，单位是expx所代表的单位。
     * @return 成功返回OK 失败返回null
     */
    public String set(String key, String value, String nxxx, String expx, long time) {
        String result = null;
        Jedis jedisClient = jedisPool.getResource();
        if (jedisClient == null) {
            return result;
        }
        try {
            result = jedisClient.set(key, value, nxxx, expx, time);
        } catch (Exception e) {
            LOGGER.error("redis set error ", e);
        } finally {
            jedisClient.close();
        }
        return result;
    }
}
