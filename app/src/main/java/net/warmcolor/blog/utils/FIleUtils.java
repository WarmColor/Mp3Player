package net.warmcolor.blog.utils;

import android.os.Environment;

import java.io.IOException;

public class FIleUtils {
    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }

    public FIleUtils() {
        // 得到当前外部存车设备的目录
        // /SDCARD
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }
    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */

}
