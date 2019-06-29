package com.momo.curator.service.impl;

import com.momo.curator.ZkLock.ZkLock;
import com.momo.curator.service.ItemsService;
import com.momo.curator.service.OrdersService;
import com.momo.curator.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    final static Logger log = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ZkLock zkLock;
    @Override
    public boolean buy(String itemId) {
        zkLock.getLock();
        int buyCounts = 6;
        // 1. 判断库存
        int stockCounts = itemsService.getItemCounts(itemId);
        if (stockCounts < buyCounts) {
            log.info("库存剩余{}件，用户需求量{}件，库存不足，订单创建失败...",
                    stockCounts, buyCounts);
            // 释放锁，让下一个请求获得锁
         zkLock.releaseLock();
            return false;
        }

        // 2. 创建订单
        boolean isOrderCreated = ordersService.createOrder(itemId);

        // 模拟处理业务需要3秒
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            zkLock.releaseLock();
        }

        // 3. 创建订单成功后，扣除库存
        if (isOrderCreated) {
            log.info("订单创建成功...");
            itemsService.displayReduceCounts(itemId, buyCounts);
        } else {
            log.info("订单创建失败...");
            // 释放锁，让下一个请求获得锁
            zkLock.releaseLock();
            return false;
        }
        // 释放锁，让下一个请求获得锁
        zkLock.releaseLock();
        return true;
    }
}
