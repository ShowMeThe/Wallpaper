package com.photo.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.photo.wallpaper.Constant.livePaper_requestCode;

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
        getScreenWH(context);
    }


    private LivePaperCallBack livePaperCallBack;

    public LivePaperCallBack getLivePaperCallBack() {
        return livePaperCallBack;
    }

    public void setLivePaperCallBack(LivePaperCallBack livePaperCallBack) {
        this.livePaperCallBack = livePaperCallBack;
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        if(client.livePaperCallBack!=null){
            if(requestCode == livePaper_requestCode && resultCode == RESULT_OK){
                client.livePaperCallBack.onSuccess();
            }else if(requestCode == livePaper_requestCode && resultCode == RESULT_CANCELED){
                client.livePaperCallBack.onFailed();
            }
        }

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

    public void setLiveImageWallpaper(Activity context,@NonNull File file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(Activity context,@NonNull Bitmap file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(Activity context, @NonNull InputStream file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveVideoWallpaper(Activity context,@NonNull File file){
        checkInit();
        try {
            wallPaper.setLiveVideoWallpaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setLiveImageWallpaper(Fragment context,@NonNull File file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(Fragment context,@NonNull Bitmap file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveImageWallpaper(Fragment context, @NonNull InputStream file){
        checkInit();
        try {
            wallPaper.setImageWallPaper(context,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLiveVideoWallpaper(Fragment context, @NonNull File file){
        checkInit();
        try {
            wallPaper.setLiveVideoWallpaper(context,file);
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
