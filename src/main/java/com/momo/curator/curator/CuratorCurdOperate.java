package com.momo.curator.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;

/**
 * Curator增删改查代码演示
 */
public class CuratorCurdOperate {
    private CuratorFramework client = null;
    private static final String zkServerPath = "114.115.215.115:2181";
    public CuratorCurdOperate() {
    }
    public CuratorCurdOperate(String zkServerPath) {
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




    public static void main(String[] args) throws Exception {
        // 实例化
        CuratorCurdOperate curatorConnect = new CuratorCurdOperate(zkServerPath);
        boolean isZkCuratorStarted = curatorConnect.getClient().isStarted();
        System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "已连接" : "已关闭"));
        /**
         * 创建节点，支持递归

        String nodePath = "/super/momo";
		byte[] data = "momo".getBytes();
        curatorConnect.getClient().create()
                .creatingParentsIfNeeded()
			    .withMode(CreateMode.PERSISTENT)
			    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
			    .forPath(nodePath, data);
         */

        /**
         * 更新节点数据
        String nodePath = "/super/momo";
        byte[] newData = "momo1".getBytes();
        curatorConnect.getClient().setData()
                 .withVersion(0)
                 .forPath(nodePath, newData);
        */

        /**
         * 查询节点
        String nodePath = "/super/momo";
		Stat stat = new Stat();
		byte[] data = curatorConnect.getClient().getData()
                        .storingStatIn(stat)
                        .forPath(nodePath);
		System.out.println("节点" + nodePath + "的数据为: " + new String(data));
		System.out.println("该节点的版本号为: " + stat.getVersion());
         */


        /**
         *   查询子节点
        String nodePath = "/super/momo";
		List<String> childNodes = curatorConnect.getClient()
                                    .getChildren()
                                    .forPath(nodePath);
		System.out.println("开始打印子节点：");
		for (String s : childNodes) {
			System.out.println(s);
		}
         */

		/**
         *判断节点是否存在,如果不存在则为空

        String nodePath = "/super/momo";
		Stat statExist = curatorConnect.getClient()
                .checkExists()
                .forPath(nodePath );
		System.out.println(statExist);
         */

        /**
         *  删除节点
        String nodePath = "/super/momo";
        curatorConnect.getClient().delete()
                      .guaranteed()					// 如果删除失败，那么在后端还是继续会删除，直到成功
                      .deletingChildrenIfNeeded()	// 如果有子节点，就删除
                      .withVersion(1)
                      .forPath(nodePath);
         */

        /**
         * watcher 事件  当getData时使用usingWatcher, 监听只会触发一次，监听完毕后就销毁
        String nodePath = "/super/momo";
        curatorConnect.getClient()
                .getData()
                .usingWatcher(new MyCuratorWatcher()).forPath(nodePath);
         */


        /**
         *  NodeCache: 监听当前节点的变更，增删改都会会触发事件
         *  创建只有说删除之后创建同名节点才会被触发，比如下面的例子只有创建“/super/momo”才会被触发

        String nodePath = "/super/momo";
		final NodeCache nodeCache = new NodeCache(curatorConnect.getClient(), nodePath);
		nodeCache.start(true);// buildInitial : 初始化的时候获取node的值并且缓存
		if (nodeCache.getCurrentData() != null) {
			System.out.println("节点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
		} else {
			System.out.println("节点初始化数据为空...");
		}
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				if (nodeCache.getCurrentData() == null) {
					System.out.println("空");
					return;
				}
				String data = new String(nodeCache.getCurrentData().getData());
				System.out.println("节点路径：" + nodeCache.getCurrentData().getPath() + "数据：" + data);
			}
		});
         */


        Thread.sleep(100000);
        curatorConnect.closeZKClient();
        boolean status = curatorConnect.getClient().isStarted();
        System.out.println("当前客户的状态：" + (status ? "已连接" : "已关闭"));
    }
}

/**
 * CuratorWatcher单次监听类
 */
class MyCuratorWatcher implements CuratorWatcher {
        @Override
        public void process(WatchedEvent event) throws Exception {
            System.out.println("触发watcher，节点路径为：" + event.getPath());
        }
    }