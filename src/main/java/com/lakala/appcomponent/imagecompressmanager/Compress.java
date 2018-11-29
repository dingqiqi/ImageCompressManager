package com.lakala.appcomponent.imagecompressmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.lakala.appcomponent.imagecompressmanager.intel.onCompileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

class Compress {

    private static Executor mExecutor = Executors.newFixedThreadPool(1);

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void ImageCompress(Context context, File file, final onCompileListener listener) {

        List<File> files = new ArrayList<>();
        files.add(file);

        ImageCompress(context, files, listener);

    }

    public static void ImageCompress(final Context context, final List<File> files, final onCompileListener listener) {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Luban.with(context)
                        .load(files)
                        .ignoreBy(ImageManager.mIgnoreSize)
                        .setTargetDir(ImageManager.mSavePath)
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                //  压缩开始前调用，可以在方法内启动 loading UI
                                if (listener != null) {

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onStart();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onSuccess(final File file) {
                                //  压缩成功后调用，返回压缩后的图片文件
                                if (listener != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onSuccess(file);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(final Throwable e) {
                                //  当压缩过程出现问题时调用
                                if (listener != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onError(e);
                                        }
                                    });
                                }
                            }
                        }).launch();
            }
        });
    }
}
