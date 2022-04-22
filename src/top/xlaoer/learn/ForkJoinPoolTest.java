package top.xlaoer.learn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author Xlaoer
 * @date 2022/4/22 15:02
 */
public class ForkJoinPoolTest {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyForkJoinPoolTask myForkJoinPoolTask = new MyForkJoinPoolTask(0,100000000);
        long time1 = System.currentTimeMillis();
        ForkJoinTask<Integer> submit = forkJoinPool.submit(myForkJoinPoolTask);
        try {
            System.out.println(submit.get());
            long time2 = System.currentTimeMillis();
            System.out.println(time2-time1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int sum = 0;
        long time3 = System.currentTimeMillis();
        for(int i=0;i<=100000000;i++){
            sum+=i;
        }
        System.out.println(sum);
        long time4 = System.currentTimeMillis();
        System.out.println(time4-time3);

    }
}
//计算和
class MyForkJoinPoolTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 20000;
    private int start;
    private int end;

    public MyForkJoinPoolTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if(end-start<=THRESHOLD){
            int sum = 0;
            for(int i=start;i<=end;i++){
                sum+=i;
            }
            return sum;
        }else{
            int mid = (end+start)/2;
            MyForkJoinPoolTask task1 = new MyForkJoinPoolTask(start, mid);
            MyForkJoinPoolTask task2 = new MyForkJoinPoolTask(mid+1,end);
            task1.fork();
            task2.fork();
            int a = task1.join();
            int b = task2.join();
            return a+b;
        }
    }
}
