package reshrink.zcw.com.study.utils;

import java.io.File;

/**
 * Created by lenovo on 2018/3/11.
 */

public class FileUtils {

    /**
     * 删除指定路径的文件
     *
     * @param filePath 文件路径
     */
    public static boolean deleteFile(String filePath) {
        try {
            if (filePath == null) {
                return false;
            }
            File file = new File(filePath);
            if (file!=null&&file.exists()) {
                file.delete();
                return  true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
        return  true;
    }
}
