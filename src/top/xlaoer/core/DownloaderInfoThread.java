package top.xlaoer.core;

/**
 * @author Xlaoer
 * @date 2022/4/16 16:51
 */

import top.xlaoer.constant.Constant;

/**
 * 展示下载信息
 */
public class DownloaderInfoThread implements Runnable{
    //下载文件总大小
    private long httpFileContentLength;

    //当前已经下载文件大小
    private long alreadyDownloadFile;

    //当前下载文件数
    public volatile long thisDownloadFile;

    //上一秒下载文件数
    private long lastDownloadFile;


    public DownloaderInfoThread(long httpFileContentLength) {
        this.httpFileContentLength = httpFileContentLength;
    }

    @Override
    public void run() {
        alreadyDownloadFile+=lastDownloadFile;

        String alreadyDownloadFileInfo = String.format("%.2f",alreadyDownloadFile/Constant.MB);
        String httpFileContentLengthInfo = String.format("%.2f",httpFileContentLength/Constant.MB);

        double speed = thisDownloadFile/Constant.MB;
        String speedInfo = String.format("%.2f", speed);

        long remainingFile = httpFileContentLength - alreadyDownloadFile;
        String remainingTimeInfo;
        if(speed>0){
            double remainingTime = remainingFile/Constant.MB/speed;
            remainingTimeInfo = String.format("%.2f", remainingTime);
        }else{
            remainingTimeInfo = "INF";
        }



        String info = String.format("%s线程进行%sMB/s下载,下载数据量为%s/%sMB,剩余下载时间%s秒", Thread.currentThread().getName(), speedInfo,alreadyDownloadFileInfo, httpFileContentLengthInfo, remainingTimeInfo);
        System.out.print("\r");
        System.out.print(info);

        lastDownloadFile = thisDownloadFile;
        thisDownloadFile = 0;
    }
}
