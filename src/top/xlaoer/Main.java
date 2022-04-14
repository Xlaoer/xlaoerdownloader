package top.xlaoer;

import top.xlaoer.core.Downloader;

import java.util.Scanner;

/**
 * @author Xlaoer
 * @date 2022/4/14 16:14
 */
public class Main {
    public static void main(String[] args) {
        //下载地址
        String url = null;
        while(url==null){
            System.out.println("请输入url:");
            //Scanner获取用户在控制台输入的下载地址
            Scanner sc = new Scanner(System.in);
            url = sc.next();
        }

        Downloader downloader = new Downloader();
        downloader.download(url);


    }
}
