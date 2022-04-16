package top.xlaoer.util;

import java.io.File;

/**
 * @author Xlaoer
 * @date 2022/4/16 20:24
 */
public class FileUtils {
    /**
     * 获得本地文件大小，如果不存在返回0
     * @param path
     * @return
     */
    public static long getLocalFileContentLength(String path){
        File file = new File(path);
        return file.exists() && file.isFile()?file.length():0;
    }
}
