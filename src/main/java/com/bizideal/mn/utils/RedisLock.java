package com.bizideal.mn.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author : liulq
 * @date: 创建时间: 2018/3/2 13:38
 * @version: 1.0
 * @Description: redis分布式锁工具类，可设置获取锁时间
 */
public class RedisLock {

    private JedisOperation jedisOperation;

    public RedisLock(JedisOperation jedisOperation) {
        this.jedisOperation = jedisOperation;
    }

    public void tryLock(String key) throws InterruptedException {
        tryLock(key, 0, TimeUnit.SECONDS);
    }

    /**
     * @param key      获取锁的key
     * @param time     尝试获取锁的时间
     * @param timeUnit 时间单位
     * @return true表示成功，false为失败
     * @throws InterruptedException
     */
    public boolean tryLock(String key, long time, TimeUnit timeUnit) throws InterruptedException {
        // 超时时间
        long deadeTime = TimeUnit.MILLISECONDS.convert(time, timeUnit) + System.currentTimeMillis();
        for (; ; ) {
            // redis锁设置2秒钟
            String res = jedisOperation.set(key, "", "NX", "EX", 2);
            if ("OK".equals(res)) {
                // 获取锁成功
                return true;
            } else {
                // 获取失败，准备重试
                TimeUnit.MILLISECONDS.sleep(100);
                if (deadeTime - System.currentTimeMillis() < 0) {
                    // 超时，返回false
                    return false;
                }
            }
        }
    }

    /**
     * 分布式锁解锁
     *
     * @param key 锁的key
     */
    public void unlock(String key) {
        jedisOperation.del(key);
    }

}
