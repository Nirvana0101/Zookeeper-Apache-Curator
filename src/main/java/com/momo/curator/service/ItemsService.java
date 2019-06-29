package com.momo.curator.service;

public interface ItemsService {

    int getItemCounts(String itemId);

    void displayReduceCounts(String itemId, int buyCounts);
}
