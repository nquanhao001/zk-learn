package zk;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/3.
 */
public class Zookeeper_Create_API_Sync implements Watcher {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Create_API_Sync());
        connectedSemaphore.await();

        String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//创建一个临时节点
        System.out.println(path1);

        String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);//创建一个临时序列节点

        System.out.println(path2);



    }
    public void process(WatchedEvent event) {

        connectedSemaphore.countDown();
    }
}
