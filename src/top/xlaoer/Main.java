package top.xlaoer;

import top.xlaoer.core.Downloader;
import top.xlaoer.util.LogUtils;

import java.util.Scanner;

/**
 * @author Xlaoer
 * @date 2022/4/14 16:14
 */
public class Main {
    public static void main(String[] args) {
        //下载地址
//        String url = null;
        String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.5.9/QQ9.5.9.28650.exe";
        while(url==null){
            LogUtils.info("请输入下载地址url:");
            //Scanner获取用户在控制台输入的下载地址
            Scanner sc = new Scanner(System.in);
            url = sc.next();
        }

        Downloader downloader = new Downloader();
        downloader.download(url);


    }
}
