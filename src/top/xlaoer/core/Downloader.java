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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 下载器
 */
public class Downloader {

    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(    InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(httpFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                ) {
            int len = -1;
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while((len=bis.read(buffer))!=-1){
                infoThread.thisDownloadFile+=len;
                bos.write(buffer,0,len);
            }


        }catch (FileNotFoundException e){
            LogUtils.error("下载的文件不存在{}",url);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.error("下载失败");
        }finally {
            System.out.print("\r");
            System.out.print("下载完成");
            //关闭连接对象
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            pool.shutdownNow();
        }

    }
}
