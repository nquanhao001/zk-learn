package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_GetChildren_API_ASync implements Watcher {  //childrenCallback  和 children2Callback 的区别只是是否带上stat
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception{
        zooKeeper = new ZooKeeper(Constants.HOST,5000 , new Zookeeper_GetChildren_API_ASync());
        connectedSemaphore.await();
        zooKeeper.create("/zk-book" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);
        zooKeeper.create("/zk-book/c1" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);

        //异步获取children
        zooKeeper.getChildren("/zk-book" , true , new IChildren2Callback() , "上下文");

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


class IChildren2Callback implements AsyncCallback.Children2Callback{
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("rc:" + rc + " path:" + path + "ctx:" + ctx + "children:" + children + "stat:" + stat);
    }
}
