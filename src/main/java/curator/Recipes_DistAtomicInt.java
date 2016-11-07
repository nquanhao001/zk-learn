package curator;

import common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by niuquanhao on 2016/11/5.
 */
//分布式计数器
public class Recipes_DistAtomicInt {


    static String path = "/curator_recipes_distatomicint_path";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder().
                connectString(Constants.HOST).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                build();
        client.start();

        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client , path , new RetryNTimes(3 , 1000));
        AtomicValue<Integer> rc = atomicInteger.add(8);
        System.out.println(rc.succeeded());
    }
}
