package com.bizideal.mn;

import com.bizideal.mn.config.SpringUtil;
import com.bizideal.mn.service.SignService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@EnableAsync // 允许异步调用
@EnableScheduling // 允许定时任务
@MapperScan("com.bizideal.mn.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    // 5个线程同时去请求签到接口
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        SignService signService = (SignService) SpringUtil.getBean("signService");
        CountDownLatch leader = new CountDownLatch(1);
        CountDownLatch order = new CountDownLatch(5);
        MyThread t1 = new MyThread("线程1", signService, leader, order);
        MyThread t2 = new MyThread("线程2", signService, leader, order);
        MyThread t3 = new MyThread("线程3", signService, leader, order);
        MyThread t4 = new MyThread("线程4", signService, leader, order);
        MyThread t5 = new MyThread("线程5", signService, leader, order);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        leader.countDown();
        order.await();
        System.out.println("ok....");
    }

    static class MyThread extends Thread {

        private SignService signService;
        private CountDownLatch leader;
        private CountDownLatch order;

        public MyThread(String threadName, SignService signService, CountDownLatch leader, CountDownLatch order) {
            super(threadName);
            this.signService = signService;
            this.leader = leader;
            this.order = order;
        }

        @Override
        public void run() {
            try {
                leader.await();
                for (int i = 0; i < 5; i++) {
                    signService.sign(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                order.countDown();
            }
        }

        public SignService getSignService() {
            return signService;
        }

        public MyThread setSignService(SignService signService) {
            this.signService = signService;
            return this;
        }

        public CountDownLatch getOrder() {
            return order;
        }

        public MyThread setOrder(CountDownLatch order) {
            this.order = order;
            return this;
        }

        public CountDownLatch getLeader() {
            return leader;
        }

        public MyThread setLeader(CountDownLatch leader) {
            this.leader = leader;
            return this;
        }
    }
}
