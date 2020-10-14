package com.photo.wallpaper;

import android.view.SurfaceHolder;

public interface IWrapper {

    void initMediaSource(SurfaceHolder holder);

    void start();

    void pause();

    void destroy();

}
