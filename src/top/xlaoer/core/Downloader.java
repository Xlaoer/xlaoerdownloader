package top.xlaoer.core;

/**
 * @author Xlaoer
 * @date 2022/4/14 16:36
 */

import top.xlaoer.constant.Constant;
import top.xlaoer.util.FileUtils;
import top.xlaoer.util.HttpUtils;
import top.xlaoer.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 下载器
 */
public class Downloader {

    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Constant.THREAD_NUM,Constant.THREAD_NUM,0,TimeUnit.SECONDS,new ArrayBlockingQueue<>(Constant.THREAD_NUM));

    public void download(String url){
        //获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);
        //拼接下载路径
        httpFileName = Constant.PATH+httpFileName;

        //获得本地文件
        long localFileContentLength = FileUtils.getLocalFileContentLength(httpFileName);

        //获取连接对象
        HttpURLConnection httpURLConnection = null;
        DownloaderInfoThread infoThread = null;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
            long contentTotalLength = httpURLConnection.getContentLengthLong();
            //判断文件是否已下载过
            if (localFileContentLength >= contentTotalLength) {
                LogUtils.info("{}已下载完毕，无需重新下载",httpFileName);
                return;
            }
            infoThread = new DownloaderInfoThread(contentTotalLength);
            pool.scheduleAtFixedRate(infoThread,1,1, TimeUnit.SECONDS);

            //切分任务
            ArrayList<Future> list = new ArrayList<>();
            split(url,list);
            for(Future future : list){
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.print("\r");
            System.out.print("下载完成");
            //关闭连接对象
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            pool.shutdownNow();
            threadPoolExecutor.shutdown();
            try {
                //如果一分钟后还没有关闭，则强制关闭
                if(!threadPoolExecutor.awaitTermination(1,TimeUnit.MINUTES)){
                    threadPoolExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 文件切分
     * @param url
     * @param futureArrayList
     */
    public void split(String url, ArrayList<Future> futureArrayList){
        try {
            long contentLength = HttpUtils.getHttpFileContentLength(url);

            //计算切分后的文件大小
            long size = contentLength/Constant.THREAD_NUM;
            for(int i=0;i<Constant.THREAD_NUM;i++){
                long startPos = i*size;
                long endPos;
                if(i == Constant.THREAD_NUM-1){
                    endPos=0;
                }else{
                    endPos=startPos+size;
                }
                if(startPos!=0){
                    startPos++;
                }

                //创建任务对象
                DownloaderTask downloaderTask = new DownloaderTask(url, startPos, endPos, i);
                Future<Boolean> future = threadPoolExecutor.submit(downloaderTask);
                futureArrayList.add(future);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
