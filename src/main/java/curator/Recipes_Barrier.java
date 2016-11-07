package curator;

import common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by niuquanhao on 2016/11/5.
 */
public class Recipes_Barrier {

    static String path = "/curator_recipes_barrier_path";

    static DistributedBarrier distributedBarrier;
    public static void main(String[] args) throws Exception{


        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder().
                                connectString(Constants.HOST).
                                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                                build();
                        client.start();
                        distributedBarrier = new DistributedBarrier(client, path);
                        System.out.println(Thread.currentThread().getName()+"号设置");
                        distributedBarrier.setBarrier();
                        distributedBarrier.waitOnBarrier();
                        System.out.println(Thread.currentThread().getName() + "启动。。。");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        Thread.sleep(1000 * 2);

        distributedBarrier.removeBarrier();
    }

}
