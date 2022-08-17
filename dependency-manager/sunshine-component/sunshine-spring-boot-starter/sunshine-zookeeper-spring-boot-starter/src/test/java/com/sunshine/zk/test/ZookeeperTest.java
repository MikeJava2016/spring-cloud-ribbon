package com.sunshine.zk.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/19 下午 12:15
 **/
@Slf4j
public class ZookeeperTest {
    private static ZooKeeper zooKeeper = null;
    public static void main(String[] args) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        String path = "/124";
        // k1
        Watcher watcher = watchedEvent -> {

            log.info("conneting...zk");
            System.out.println("线程 = " + Thread.currentThread().getName());
            Watcher.Event.EventType eventType = watchedEvent.getType();
            Watcher.Event.KeeperState eventState = watchedEvent.getState();
            String watchedEventPath = watchedEvent.getPath();
            // 第一次触发
            if (eventState == Watcher.Event.KeeperState.SyncConnected && eventType == Watcher.Event.EventType.None && watchedEventPath == null) {
                countDownLatch.countDown();
            } else if (path.equals(watchedEventPath) && eventType == Watcher.Event.EventType.NodeDeleted) {
                // 如果节点被删除，业务处理在这
            } else if (path.equals(watchedEventPath) && eventType == Watcher.Event.EventType.NodeDataChanged) {
                try {
                    zooKeeper.getData(watchedEventPath, true, null);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 业务操作
            log.info("业务操作");
        };
        zooKeeper = new ZooKeeper("localhost:2181", 15000, watcher);


        if (zooKeeper.exists(path,null) == null){
            zooKeeper.create(path,"del".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        countDownLatch.await();

        zooKeeper.getData(path, watcher,null);



        zooKeeper.setData(path, "de2".getBytes(), -1);
        zooKeeper.setData(path, "de22".getBytes(), -1);
        Thread.sleep(Long.MAX_VALUE);
//        zooKeeper.close();
    }
}
