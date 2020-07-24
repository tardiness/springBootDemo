package com.test.zookeeper.biz;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2019/4/4
 * @modifyDate: 13:57
 * @Description:
 */
public class AclCreate implements Watcher {

    private static  CountDownLatch count = new CountDownLatch(1);

    private static  CountDownLatch countDownLatch = new CountDownLatch(1);

    private static  ZooKeeper zk = null;

    public void syncInit() {
        try {
            zk = new ZooKeeper("127.0.0.1",5000,new AclCreate());
            count.await();
            zk.addAuthInfo("digest","admin:123456".getBytes());
            zk.create("/act","init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

            ZooKeeper zk3 = new ZooKeeper("127.0.0.1",5000,null);
            zk3.addAuthInfo("digest","admin:123456".getBytes());
            String value2 = new String(zk3.getData("/act",false,null));

            System.out.println("zk3有权限进行数据的获取" + value2);

            ZooKeeper zk2 =  new ZooKeeper("127.0.0.1", 5000,
                    null);
            zk2.addAuthInfo("digest", "super:123".getBytes());
            zk2.getData("/act", false, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            System.out.println("异常:" + e.getMessage());
            System.out.println("zk2没有权限进行数据的获取");
            countDownLatch.countDown();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (event.getType() == Event.EventType.None && null == event.getPath()) {
                count.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AclCreate aclCreate = new AclCreate();
        aclCreate.syncInit();
        countDownLatch.await();
    }


}
