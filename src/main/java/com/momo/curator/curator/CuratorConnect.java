package com.momo.curator.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * Curator连接zkServer代码演示
 */
public class CuratorConnect {
    private CuratorFramework client = null;
    private static final String zkServerPath = "114.115.215.115:2181";
    public CuratorConnect() {
    }
    public CuratorConnect(String zkServerPath) {
        /**
         * n：重试的次数
         * sleepMsBetweenRetries：每次重试间隔的时间
         */
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }
    /**
     *
     * @Description: 关闭zk客户端连接
     */
    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }
    public CuratorFramework getClient() {
        return client;
    }
    public void setClient(CuratorFramework client) {
        this.client = client;
    }
    public static void main(String[] args) {
        // 实例化
        CuratorConnect curatorConnect = new CuratorConnect(zkServerPath);
        boolean isZkCuratorStarted = curatorConnect.getClient().isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "已连接" : "已关闭"));
        curatorConnect.closeZKClient();
        boolean status = curatorConnect.getClient().isStarted();
        System.out.println("当前客户的状态：" + (status ? "已连接" : "已关闭"));
    }
}
