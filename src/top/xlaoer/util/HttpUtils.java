package top.xlaoer.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Xlaoer
 * @date 2022/4/14 16:28
 */
public class HttpUtils {
    /**
     * 分块下载
     * @param url   下载地址
     * @param startPos  下载文件起始位置
     * @param endPos    下载文件的结束位置
     * @return
     */
    public static HttpURLConnection getHttpURLConnection(String url,long startPos,long endPos) throws IOException {
        HttpURLConnection httpURLConnection = getHttpURLConnection(url);
        LogUtils.info("下载的区间为：{}-{}",startPos,endPos);

        if(endPos!=0){
            httpURLConnection.setRequestProperty("RANGE","bytes="+startPos+"-"+endPos);
        }else{
            //说明下载的是最后一块区域，会把所有内容返回
            httpURLConnection.setRequestProperty("RANGE","bytes="+startPos+"-");
        }
        return httpURLConnection;
    }

    /**
     * 获取HttpURLConnection
     * @param url
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection httpUrlConnection = (HttpURLConnection)httpUrl.openConnection();

        //模拟客户端
        httpUrlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");

        return httpUrlConnection;
    }

    /**
     * 获取下载文件的名字
     * @param url
     * @return
     */
    public static String getHttpFileName(String url){
        int index = url.lastIndexOf('/');
        return url.substring(index+1);
    }

}
