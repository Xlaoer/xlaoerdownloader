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
