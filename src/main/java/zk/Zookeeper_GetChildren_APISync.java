package zk;

import common.Constants;
import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_GetChildren_APISync implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(Constants.HOST,5000 , new Zookeeper_GetChildren_APISync());
        connectedSemaphore.await();
        zooKeeper.create("/zk-book" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
        zooKeeper.create("/zk-book/c1" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);

        //同步获取children的接口
        List<String> children = zooKeeper.getChildren("/zk-book", true);
        System.out.println(children);

        zooKeeper.create("/zk-book/c2" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);


        Thread.sleep(1000 * 30);

    }


    public void process(WatchedEvent event) {

        if (Event.KeeperState.SyncConnected.equals(event.getState())){
            if (Event.EventType.None.equals(event.getType()) && event.getPath() == null){
                //建立连接的事件
                connectedSemaphore.countDown();
            }else if (Event.EventType.NodeChildrenChanged.equals(event.getType())){

                try {
                    System.out.println("reGet child:" + zooKeeper.getChildren(event.getPath() , true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
