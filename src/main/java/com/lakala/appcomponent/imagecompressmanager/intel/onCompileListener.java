package com.lakala.appcomponent.imagecompressmanager.intel;

import java.io.File;

public interface onCompileListener {

    void onStart();

    void onSuccess(File file);

    void onError(Throwable e);
}
