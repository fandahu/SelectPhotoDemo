package com.zydl.selectphotodemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 路径工具类
 * Created by Genius on 2017/6/28.
 */

public class PathUtil {
    /**
     * 获取sd卡地址
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
            return sdDir.toString();
        } else {
            return "-1";
        }
    }

    /**
     * 获取应用程序数据包根目录
     *
     * @return
     */
    public static String getAppDirPath(Context context) {
        String _SDPath = getSDPath();
        String dirPathString = _SDPath + "/" + context.getString(R.string.app_name);
        File file = new File(dirPathString);
        if (!file.exists()) {
            file.mkdir();
        }
        return dirPathString;
    }

    /**
     * 获取图片根目录
     *
     * @return
     */
    public static String getPicDirPath(Context context) {
        String picDirPathString = getAppDirPath(context) + "/Pic";
        File file = new File(picDirPathString);
        if (!file.exists()) {
            file.mkdir();
        }
        return picDirPathString;
    }

    /**
     * 获取图片根目录
     *
     * @return
     */
    public static String getCachePicPath(Context context) {
        String picDirPathString = getAppDirPath(context) + "/CachePic";
        File file = new File(picDirPathString);
        if (!file.exists()) {
            file.mkdir();
        }
        return picDirPathString;
    }

    /**
     * 获取视频根目录
     *
     * @return
     */
    public static String getVideoDirPath(Context context) {
        String videoDirPathString = getAppDirPath(context) + "/Video";
        File file = new File(videoDirPathString);
        if (!file.exists()) {
            file.mkdir();
        }
        return videoDirPathString;
    }

    /**
     * 获取更新安装包根目录
     *
     * @return
     */
    public static String getUpdateAppPath(Context context) {
        String picDirPathString = getAppDirPath(context) + "/UploadApp";
        File file = new File(picDirPathString);
        if (!file.exists()) {
            file.mkdir();
        }
        return picDirPathString;
    }


}
