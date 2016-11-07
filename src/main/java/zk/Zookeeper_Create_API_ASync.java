package zk;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/3.
 */
//异步创建节点
public class Zookeeper_Create_API_ASync implements Watcher{

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);


    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181" , 5000 , new Zookeeper_Create_API_ASync());
        connectedSemaphore.await();

        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL , new IStringCallback() ,"传递的上下文");//创建一个临时节点
        zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL , new IStringCallback() ,"传递的上下文2");//创建一个临时节点


        Thread.sleep(1000 * 20);
    }




    public void process(WatchedEvent event) {
        connectedSemaphore.countDown();
    }
}

class IStringCallback implements AsyncCallback.StringCallback{
    public void processResult(int rc, String path, Object ctx, String name) {
        //rc  代表服务端响应吗  0:调用成功  -4:客户端和服务端连接断开  -110:节点已存在  -112:回话过期
        //path  创建时候指定的path参数值
        //ctx  创建时候传递的ctx值
        //name   服务端实际的节点名
        System.out.println(String.format("rc:%s path:%s ctx:%s name:%s" , rc,path,ctx,name));//TODO  为什么 临时节点也会 序列值每次增加  而且是+2
    }
}
