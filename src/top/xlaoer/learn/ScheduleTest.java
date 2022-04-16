package top.xlaoer.learn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Xlaoer
 * @date 2022/4/16 16:33
 */
public class ScheduleTest {
    public static void main(String[] args) {
        scheduleAtFixedRate();
    }

    public static void scheduleAtFixedRate(){
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        //延时两秒之后开始执行任务，间隔三秒执行任务
        pool.scheduleAtFixedRate(()-> System.out.println(System.currentTimeMillis()),2,3,TimeUnit.SECONDS);

        //pool.shutdown();
    }

    public static void schedule(){
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        //延时两秒之后再执行任务
        pool.schedule(()-> System.out.println(Thread.currentThread().getName()),2, TimeUnit.SECONDS);

        pool.shutdown();
    }

}
