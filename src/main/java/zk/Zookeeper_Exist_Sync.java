package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_Exist_Sync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws Exception{

        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constants.HOST , 5000 , new Zookeeper_Exist_Sync());
        connectedSemaphore.await();

        Stat stat = zooKeeper.exists(path, true);//同步检测是否存在
        System.out.println(stat);

        zooKeeper.create(path , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT);//同步创建

        zooKeeper.setData(path , "123".getBytes() , -1);//同步修改数据

        zooKeeper.create(path + "/c1" , "".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);//同步创建子节点

        zooKeeper.delete(path + "/c1" , -1);

        zooKeeper.delete(path  , -1);


        Thread.sleep(1000 * 10);



    }

    public void process(WatchedEvent event) {

        try {
            if (Event.KeeperState.SyncConnected.equals(event.getState())){
                if (event.getPath() == null && Event.EventType.None.equals(event.getType())){
                    connectedSemaphore.countDown();
                }
                if (Event.EventType.NodeCreated.equals(event.getType())){
                    System.out.println(event.getPath() +"created");
                    zooKeeper.exists(event.getPath() , true);
                }

                if (Event.EventType.NodeDeleted.equals(event.getType())){
                    System.out.println(event.getPath() +"deleted");
                    zooKeeper.exists(event.getPath() , true);
                }

                if (Event.EventType.NodeDataChanged.equals(event.getType())){
                    System.out.println(event.getPath() +"dataChanged");
                    zooKeeper.exists(event.getPath() , true);
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
