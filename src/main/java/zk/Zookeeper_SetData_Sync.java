package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_SetData_Sync implements Watcher {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;


    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constants.HOST , 5000 , new Zookeeper_SetData_Sync());
        connectedSemaphore.await();

        zooKeeper.create(path , "123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        System.out.println(new String(zooKeeper.getData(path , true , null)));

        Stat stat = zooKeeper.setData(path, "456".getBytes(), -1);
        System.out.println(stat);

        Stat stat2 = zooKeeper.setData(path, "456".getBytes(), stat.getVersion());

        System.out.println(stat2);

        try {
            zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    public void process(WatchedEvent event) {

        if (Event.KeeperState.SyncConnected.equals(event.getState())){
            if (Event.EventType.None.equals(event.getType()) && event.getPath() == null){
                //建立连接的事件
                connectedSemaphore.countDown();
            }

        }
    }
}
