package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
//同步进行获取数据
public class Zookeeper_GetData_API_Sync implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constants.HOST , 5000 , new Zookeeper_GetData_API_Sync());
        connectedSemaphore.await();

        zooKeeper.create(path , "123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

        System.out.println(new String(zooKeeper.getData(path , true , stat)));
        System.out.println(stat);

        zooKeeper.setData(path , "222".getBytes() , -1);

        Thread.sleep(1000 * 30);

    }


    public void process(WatchedEvent event) {

        if (Event.KeeperState.SyncConnected.equals(event.getState())){
            if (Event.EventType.None.equals(event.getType()) && event.getPath() == null){
                //建立连接的事件
                connectedSemaphore.countDown();
            }else if (Event.EventType.NodeDataChanged.equals(event.getType())){//数据或者版本发生了变化

                try {
                    System.out.println(new String(zooKeeper.getData(event.getPath() , true , stat)));
                    System.out.println(stat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
