package com.photo.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class WallpaperClient {


    private WallpaperClient(){
    }

    private  static WallpaperClient client;

    private WallpaperManager manager;

    private WeakReference<Context> context;

    private LiveWallPaper wallPaper;


    public static WallpaperClient getClient(){
        if(client == null){
            synchronized (WallpaperClient.class){
                if(client == null){
                    client = new WallpaperClient();
                }

            }
        }

        return client;
    }

    private void  checkInit(){
        if(context == null || manager == null|| wallPaper == null){
            throw new IllegalStateException("Please init first");
        }
    }

    public void doInitFirst(Context context){
        this.context = new WeakReference<>(context);
        manager = WallpaperManager.getInstance(context);
        wallPaper = new LiveWallPaper();
    }

    public void setImageWallpaper(@NonNull Bitmap bitmap){
        checkInit();
        try {
            manager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WeakReference<Context> getContext() {
        return context;
    }

    public void setLiveImageWallpaper(@NonNull File file,ICallBack callBack){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file,callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(@NonNull Bitmap file,ICallBack callBack){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file,callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(@NonNull InputStream file,ICallBack callBack){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file,callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveVideoWallpaper(@NonNull File file,ICallBack callBack){
        checkInit();
        try {
            wallPaper.setLiveVideoWallpaper(context.get(),file,callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
