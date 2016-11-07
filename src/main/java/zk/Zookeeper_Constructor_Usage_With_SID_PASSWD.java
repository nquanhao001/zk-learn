package zk;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/3.
 */
//使用sessionId和pwd 能够复用session
public class Zookeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {//TODO  后面创建的zk对象  和前面的 是否能一起用?
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Constructor_Usage_With_SID_PASSWD());
        System.out.println("第一次创建:" + JSON.toJSONString(zooKeeper));
        connectedSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();

        //这个是用错误的sessionId和pwd  创建的zookeeper
        ZooKeeper bad = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Constructor_Usage_With_SID_PASSWD() , 1L , "badPWD".getBytes());
        System.out.println("错误的zk:" + JSON.toJSONString(bad));


        //用正确的sessionId和pwd创建

        ZooKeeper good = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Constructor_Usage_With_SID_PASSWD() , sessionId , sessionPasswd);

        System.out.println("复用的zk:" + JSON.toJSONString(good));

        Thread.sleep(1000 * 10);

    }
    public void process(WatchedEvent event) {
        System.out.println("receive enent:" + JSON.toJSONString(event));
        if (Event.KeeperState.SyncConnected.equals(event.getState())){
            System.out.println("收到 建立连接的event");
            connectedSemaphore.countDown();//建立连接后 ,会使main进程退出
        }
    }
}
