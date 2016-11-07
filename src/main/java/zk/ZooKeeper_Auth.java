package zk;

import common.Constants;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by niuquanhao on 2016/11/4.
 */
public class ZooKeeper_Auth {

    static String PATH = "/zk-book-auth-test";

    public static void main(String[] args) throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(Constants.HOST , 5000 , null);
        zooKeeper.addAuthInfo("digest" , "niu:gege".getBytes());
        zooKeeper.create(PATH , "cdd".getBytes() , ZooDefs.Ids.CREATOR_ALL_ACL , CreateMode.EPHEMERAL);


        ZooKeeper zooKeeper2 = new ZooKeeper(Constants.HOST , 5000 , null);
        zooKeeper2.addAuthInfo("digest" , "niu:gege".getBytes());
        System.out.println(new String(zooKeeper2.getData(PATH , false , null)));

        ZooKeeper zooKeeper3 = new ZooKeeper(Constants.HOST , 5000 , null);

        zooKeeper3.getData(PATH , false , null);//没有权限
    }
}
