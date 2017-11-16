package util;

import java.io.*;

/**
 * @author yiwenqiu
 * @Description 文件操作类
 * @date 22:09 2017/11/14
 */
public class FileUtil {
    public static File isFileExist(String path){
        File file = new File(path);
        if (!file.exists()){
            System.err.println(path+" is not exist");
            return null;
        }
        return file;
    }

    public static StringBuffer fileToStringBuffer(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while((line = in.readLine())!=null){
            buffer.append(line);
        }
        return buffer;
    }
}
