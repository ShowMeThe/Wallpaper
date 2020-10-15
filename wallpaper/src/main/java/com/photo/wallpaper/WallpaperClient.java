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

    private int SCREEN_WIDTH = 0;
    private int SCREEN_HEIGHT = 0;

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

    private void getScreenWH(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
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

    public void setLiveImageWallpaper(@NonNull File file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(@NonNull Bitmap file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(@NonNull InputStream file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context.get(),file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveVideoWallpaper(@NonNull File file){
        checkInit();
        try {
            wallPaper.setLiveVideoWallpaper(context.get(),file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }
}
