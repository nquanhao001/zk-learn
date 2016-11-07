package zk;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/3.
 */
public class Zookeeper_Constructor_Usage_simple implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{//TODO  构造方法干了什么事情
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Constructor_Usage_simple());
        //和zk的服务端建立连接是异步过程,所以构造方法直接返回了。

        System.out.println(zooKeeper.getState());
        System.out.println(zooKeeper.getState());
        System.out.println(zooKeeper.getState());
        System.out.println(JSON.toJSONString(zooKeeper));
        System.out.println(JSON.toJSONString(zooKeeper));
        System.out.println(zooKeeper.getState());
        //System.out.println(JSON.toJSONString(zooKeeper));

        //等待着建立连接的event触发countDown
        connectedSemaphore.await();
    }

    public void process(WatchedEvent event) {
        System.out.println("receive enent:" + JSON.toJSONString(event));
        if (Event.KeeperState.SyncConnected.equals(event.getState())){
            connectedSemaphore.countDown();//建立连接后 ,会使main进程退出
        }
    }
}
