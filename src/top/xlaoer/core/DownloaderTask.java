package top.xlaoer.core;

import top.xlaoer.constant.Constant;
import top.xlaoer.util.HttpUtils;
import top.xlaoer.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

/**
 * @author Xlaoer
 * @date 2022/4/18 21:58
 */
public class DownloaderTask implements Callable<Boolean> {

    private String url;

    private long startPos;

    private long endPos;

    private int part;

    public DownloaderTask(String url, long startPos, long endPos, int part) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.part = part;
    }

    @Override
    public Boolean call() throws Exception {
        //获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);
        //分块的文件名
        httpFileName = httpFileName + ".temp" + part;
        //拼接下载到本地的路径
        httpFileName = Constant.PATH + httpFileName;

        //获取分块下载的连接
        HttpURLConnection httpURLConnection = HttpUtils.getHttpURLConnection(url, startPos, endPos);

        try (
                InputStream input = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                RandomAccessFile file = new RandomAccessFile(httpFileName, "rw");
        ) {
            int len = -1;
            byte[] buffer = new byte[Constant.BYTE_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                file.write(buffer, 0, len);
            }

        } catch (FileNotFoundException e) {
            LogUtils.error("下载文件不存在{}", url);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.error("下载出错");
        }


        return true;
    }
}