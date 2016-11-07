package curator;

import common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by niuquanhao on 2016/11/5.
 */
public class Recipes_barrier2 {

    static String path = "/curator_recipes_barrier_path";


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

                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client , path ,5);
                        Thread.sleep(Math.round(Math.random() * 3000));
                        System.out.println(Thread.currentThread().getName() + "进入battrier");
                        barrier.enter();
                        System.out.println(Thread.currentThread().getName() + "启动");
                        Thread.sleep(Math.round(Math.random() * 3000));
                        barrier.leave();
                        System.out.println(Thread.currentThread().getName() + "离开");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

}
