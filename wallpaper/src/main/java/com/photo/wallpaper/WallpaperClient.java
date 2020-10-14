package com.photo.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class WallpaperClient {


    private WallpaperClient(){
    }

    private  static WallpaperClient client;

    private WallpaperManager manager;

    private WeakReference<Context> context;

    private LiveWallPaper wallPaper;

    private WallpaperPool pool;

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
        if(context == null || manager == null|| wallPaper == null || pool == null){
            throw new IllegalStateException("Please init first");
        }
    }

    public void doInitFirst(Context context){
        this.context = new WeakReference<>(context);
        manager = WallpaperManager.getInstance(context);
        wallPaper = new LiveWallPaper();

        pool = new WallpaperPool(context);
    }


    public void setImageWallpaper(@NonNull Bitmap bitmap){
        checkInit();
        try {
            manager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadLiveWallpaper(String url,CallBack callBack){
        pool.download(url,callBack);
    }


    public void setLiveWallpaper(@NonNull File file){
        checkInit();
        try {
            wallPaper.setWallPaper(context.get(),file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
