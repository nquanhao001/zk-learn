package curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by niuquanhao on 2016/11/5.
 */
public class Recipes_NoLock {

    public static void main(String[] args) {
        final CountDownLatch downLatch = new CountDownLatch(1);
        for (int i = 0; i< 10 ; i++) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            downLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                        ;
                        System.out.println(sdf.format(new Date()));
                    }
                }).start();
        }
        downLatch.countDown();
    }
}
