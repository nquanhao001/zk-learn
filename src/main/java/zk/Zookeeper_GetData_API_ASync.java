package zk;

import common.Constants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_GetData_API_ASync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();
    public static void main(String[] args) throws Exception{
        String path = "/zk-book";
        zooKeeper = new ZooKeeper(Constants.HOST , 5000 , new Zookeeper_GetData_API_ASync());
        connectedSemaphore.await();

        zooKeeper.create(path , "123".getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.EPHEMERAL);

        //异步进行获取数据
        zooKeeper.getData(path , true , new IDataCallback() , "ctx");

        //再次刷新数据,触发event
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
                    //异步进行获取数据
                    zooKeeper.getData(event.getPath() , true , new IDataCallback() , "ctx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

class IDataCallback implements AsyncCallback.DataCallback{
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println("rc:" + rc + " path:" + path + "ctx:" + ctx + "data:" + new String(data) + "stat:" + stat);
    }
}
