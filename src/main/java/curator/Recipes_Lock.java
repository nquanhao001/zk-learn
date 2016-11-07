package curator;

import common.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static org.apache.curator.framework.CuratorFrameworkFactory.*;

/**
 * Created by niuquanhao on 2016/11/5.
 */
public class Recipes_Lock {

    static String lock_path = "/curator_recipes_lock_path";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.builder().
                connectString(Constants.HOST).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                build();
        client.start();

        final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatch.await();
                        lock.acquire();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    ;
                    System.out.println(sdf.format(new Date()));
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
        countDownLatch.countDown();
    }
}
