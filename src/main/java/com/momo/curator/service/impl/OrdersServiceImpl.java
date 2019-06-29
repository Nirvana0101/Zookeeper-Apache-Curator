package com.momo.curator.service.impl;

import com.momo.curator.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Override
    public boolean createOrder(String itemId) {
        return true;
    }
}
