package top.xlaoer.learn;

import java.sql.Time;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Xlaoer
 * @date 2022/4/17 20:05
 */
public class PoolTest01 {
    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
        MyTask task = new MyTask();

        try {
            for(int i=0;i<10;i++){
                poolExecutor.execute(task);
            }
            System.out.println(poolExecutor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            poolExecutor.shutdown();
        }


    }
}

class MyTask implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}
