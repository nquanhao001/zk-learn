package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_SetData_ASync implements Watcher {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;


    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constants.HOST , 5000 , new Zookeeper_SetData_ASync());
        connectedSemaphore.await();

        zooKeeper.create(path , "123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);
        System.out.println(new String(zooKeeper.getData(path , true , null)));


        zooKeeper.setData(path , "456".getBytes() , -1, new IStatCallback() , "xtc");

        Thread.sleep(1000 * 5);




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


class IStatCallback implements AsyncCallback.StatCallback{
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        System.out.println("rc:" + rc + " path:" + path + "ctx:" + ctx  + "stat:" + stat);

    }
}
