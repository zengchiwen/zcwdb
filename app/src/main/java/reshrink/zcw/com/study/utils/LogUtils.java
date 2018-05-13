package reshrink.zcw.com.study.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import reshrink.zcw.com.study.MyApp;

/**
 * Created by lenovo on 2017/7/14.
 */

public class LogUtils {

    //开关
    private static final boolean SWITCH = true;
    //TAG
    public static final String TAG = "KSD";

    //标准
    public static final String INFO = "info ";
    //警告
    public static final String WARN = "warn ";
    //错误
    public static final String ERROR = "error";

    //默认每次缩进两个空格
    private static final String empty = "  ";
    //时间格式
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    //保存路径
    public static final String PATH = "/data/data/com.ksd.carcloud/applog1/";
    //文件名


    //info
    public static void i(String content) {
        if (SWITCH) {
            Log.i(TAG, content);
        }
    }

    //debug
    public static void d(String content) {
        if (SWITCH) {
            Log.d(TAG, content);
        }
    }

    //warn
    public static void w(String content) {
        if (SWITCH) {
            Log.w(TAG, content);
        }
    }

    //error
    public static void e(String content) {
        if (SWITCH) {
            Log.e(TAG, content);
        }
    }
    /**
     * 本地日志写入文件
     * 使用方法:RecordLogUtils.printLog(RecordLogUtils.ERROR,"this is error!");
     * 权限:android.permission.WRITE_EXTERNAL_STORAGE
     *
     * @param verbose
     * @param content
     */
    public static void printLogSD(String verbose, String content) {

        if (SWITCH) {
         String FILE_NAME= Utils.dateFormatYMD(MyApp.getContext()) + ".log";
            Log.e(verbose,content);
            File f = new File(PATH);
            if (f != null && !f.exists())
                f.mkdirs();
            FileOutputStream fos = null;
            try {
                File file = new File(PATH +FILE_NAME);
                //先判断要写的目标文件是否存在，如果存在直接写，如果不存在就执行下一步
                //判断文件夹里的文件个数是否小于7，如果小于直接创建，如果大于，删除掉最小的那个再创建
                if (!file.exists()) {
                    File[] files = f.listFiles();
                    // System.out.print(files.length);

                    if (files!=null&&files.length >= 5) {
                        long min = getTime4FileName(files[0].getName());
                        for (File logFile : files) {
                            long time4FileName = getTime4FileName(logFile.getName());
                            if (min > time4FileName) {
                                min = time4FileName;
                            }
                        }
                        //删除最小日期的那个
                        FileUtils.deleteFile(PATH + min + ".log");
                        //创建最新的
                        if (file != null) {
                            file.createNewFile();
                        }
                    }

                }
                 fos = new FileOutputStream(file, true);
                fos.write((verbose + " Time : " + new SimpleDateFormat(FORMAT).format(new Date()) + " : " + content + "\n").getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                        fos = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    }


    /**
     * 根据日志文件名，获取日志
     */
    private static long getTime4FileName(String fileName) {
        if (fileName.contains(".")) {
            String time = fileName.substring(0, fileName.indexOf("."));
            return Utils.parseLong(time);
        }
        return 0;
    }


    //获取行号
    private static int getLineNumber(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return -1;
        return trace[0].getLineNumber();
    }

    //判断符号
    private static boolean isDoubleSerialBackslash(char[] chs, int i) {
        int count = 0;
        for (int j = i; j > -1; j--) {
            if (chs[j] == '\\') {
                count++;
            } else {
                return count % 2 == 0;
            }
        }
        return count % 2 == 0;
    }

    //缩进
    private static String getEmpty(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(empty);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断是否存在某个包名
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        synchronized (context) {
            PackageManager packageManager = context.getPackageManager();

            //获取手机系统的所有APP包名，然后进行一一比较
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            for (int i = 0; i < pinfo.size(); i++) {
                LogUtils.e(pinfo.get(i).packageName);
                if (((PackageInfo) pinfo.get(i)).packageName
                        .equalsIgnoreCase(packageName))
                    return true;
            }

            return false;
        }
    }

}
