package zk;

import common.Constants;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class Zookeeper_Auth_Delete {

    static String PATH = "/zk-book-auth-test";
    static String PATH_CHILD = "/zk-book-auth-test/child";

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(Constants.HOST , 5000 , null);
        zooKeeper.addAuthInfo("digest" , "niu:gege".getBytes());
        zooKeeper.create(PATH , "cdd".getBytes() , ZooDefs.Ids.CREATOR_ALL_ACL , CreateMode.PERSISTENT);//建立父节点
        zooKeeper.create(PATH_CHILD , "cdd".getBytes() , ZooDefs.Ids.CREATOR_ALL_ACL , CreateMode.EPHEMERAL);//建立子节点

        ZooKeeper zooKeeper2 = new ZooKeeper(Constants.HOST , 5000 , null);
        try {

            zooKeeper2.delete(PATH_CHILD , -1);
        } catch (Exception e) {
            System.out.println("没有权限,删除失败");
        }

        zooKeeper.delete(PATH_CHILD , -1);

        zooKeeper2.delete(PATH , -1);//这里说明没有权限的节点,依然可以被删除,但是这个节点的子节点,必须有权限才能删除
        //TODO  为什么这样设计呢???删除的权限校验逻辑


    }

}
