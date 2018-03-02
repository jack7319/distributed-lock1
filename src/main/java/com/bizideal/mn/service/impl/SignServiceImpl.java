package com.bizideal.mn.service.impl;

import com.bizideal.mn.entity.Sign;
import com.bizideal.mn.mapper.SignMapper;
import com.bizideal.mn.service.SignService;
import com.bizideal.mn.utils.JedisOperation;
import com.bizideal.mn.utils.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : liulq
 * @date: 创建时间: 2018/3/2 11:15
 * @version: 1.0
 * @Description:
 */
@Service("signService")
public class SignServiceImpl implements SignService {

    private final static Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    @Autowired
    private SignMapper signMapper;
    @Autowired
    private JedisOperation jedisOperation;

    // 签到。每人每天只能签到一次。
    @Override
    public int sign(int userId) throws Exception {
        RedisLock redisLock = new RedisLock(jedisOperation);
        boolean b = redisLock.tryLock(String.valueOf(userId), 1, TimeUnit.SECONDS);
        if (b = false) {
            logger.info("线程：{} 获取锁失败...", Thread.currentThread().getName());
            return 0;
        }
        logger.info("线程：{} 获取锁成功...", Thread.currentThread().getName());
        Sign sign = new Sign().setUserId(userId).setCreateTime(new Date());
        List<Sign> signs = signMapper.selectToday();
        if (!signs.isEmpty()) {
            throw new Exception("已签到，请勿重复签到！");
        }
        int insert = signMapper.insert(sign);
        logger.info("线程：{}，签到成功！", Thread.currentThread().getName());
        redisLock.unlock(String.valueOf(userId));
        return insert;
    }
}
