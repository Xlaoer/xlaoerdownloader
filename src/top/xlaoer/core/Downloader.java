package top.xlaoer.core;

/**
 * @author Xlaoer
 * @date 2022/4/14 16:36
 */

import top.xlaoer.constant.Constant;
import top.xlaoer.util.HttpUtils;
import top.xlaoer.util.LogUtils;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * 下载器
 */
public class Downloader {

    public void download(String url){
        //获取文件名
        String httpFileName = HttpUtils.getHttpFileName(url);
        //拼接下载路径
        httpFileName = Constant.PATH+httpFileName;

        //获取连接对象
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = HttpUtils.getHttpURLConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(    InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                FileOutputStream fos = new FileOutputStream(httpFileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ) {
            int len = -1;
            while((len=bis.read())!=-1){
                bos.write(len);
            }


        }catch (FileNotFoundException e){
            LogUtils.error("下载的文件不存在{}",url);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.error("下载失败");
        }finally {
            //关闭连接对象
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }

    }
}
