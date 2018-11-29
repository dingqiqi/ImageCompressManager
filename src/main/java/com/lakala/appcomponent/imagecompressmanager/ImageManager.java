package com.lakala.appcomponent.imagecompressmanager;

import android.content.Context;
import android.text.TextUtils;

import com.lakala.appcomponent.imagecompressmanager.intel.onCompileListener;

import java.io.File;
import java.util.List;

public class ImageManager {
    private static Context mContext;
    public static int mIgnoreSize = 100;
    public static String mSavePath;

    private static boolean isInit = false;

    /**
     * 初始化
     *
     * @param context    上下文
     * @param ignoreSize 小于这个值不压缩 单位kb 默认100kb
     * @param savePath   压缩后图片存储路径
     */
    public static void init(Context context, int ignoreSize, String savePath) {
        if (context == null || ignoreSize <= 0 || TextUtils.isEmpty(savePath)) {
            throw new IllegalArgumentException("初始化参数不能为空");
        }

        mContext = context.getApplicationContext();
        mIgnoreSize = ignoreSize;
        mSavePath = savePath;

        isInit = true;
    }

    public static void imageCompress(File file, onCompileListener listener) {
        if (!isInit) {
            throw new IllegalArgumentException("请先初始化");
        }

        Compress.ImageCompress(mContext, file, listener);
    }

    public static void imageCompress(List<File> files, onCompileListener listener) {
        if (!isInit) {
            throw new IllegalArgumentException("请先初始化");
        }

        Compress.ImageCompress(mContext, files, listener);
    }

}
