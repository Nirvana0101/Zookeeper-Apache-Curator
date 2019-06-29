package com.momo.curator.service.impl;

import com.momo.curator.service.ItemsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemsServiceImpl implements ItemsService {
    @Value("${Init.stockCounts}")
    private Integer stockCounts;
    @Override
    public int getItemCounts(String itemId) {
        return stockCounts;
    }
    @Override
    public void displayReduceCounts(String itemId, int buyCounts) {
        stockCounts-=buyCounts;
    }
}
