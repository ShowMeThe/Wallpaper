package com.photo.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.FloatRange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.photo.wallpaper.Const.ACTION_VOICE;
import static com.photo.wallpaper.Const.WALLPAPER_ACTION;

public class LiveWallPaper extends WallpaperService {

    private static final String TAG = "LiveWallPaper";

    public static boolean surfaceInitialized = false;

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();

    private ICallBack iCallBack;

    public void volume(@FloatRange(from = 0.0, to = 1.0) float volume) {
        Intent intent = new Intent();
        intent.setAction(WALLPAPER_ACTION);
        intent.putExtra(ACTION_VOICE, volume);
        getApplicationContext().sendBroadcast(intent);
    }


    public void setLiveVideoWallpaper(Context context, File videoPath,ICallBack callBack) {
        wrapper.setPlayingFile(videoPath);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.VIDEO);
        createActivity(context,callBack);
    }


    public void setImageWallPaper(Context context, InputStream imageFile,ICallBack callBack) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context,callBack);
    }

    public void setImageWallPaper(Context context, Bitmap imageFile,ICallBack callBack) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context,callBack);
    }

    public void setImageWallPaper(Context context, File imageFile,ICallBack callBack) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context,callBack);
    }


    private void createActivity(Context context,ICallBack callBack) {
        iCallBack = callBack;
        MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();
        if(wrapper.holder == null || !wrapper.holder.getSurface().isValid()){
            surfaceInitialized = false;
        }
        if(!surfaceInitialized){
            //未启动
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(context, LiveWallPaper.class));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if(callBack!=null){
                callBack.onPaperStart();
            }
        }else {
            //已启动
            wrapper.initMediaSource(wrapper.holder);
            if(callBack!=null){
                callBack.onPaperChange();
            }
        }
    }

    private class WallpaperEngine extends Engine {
        private MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();
        private VideoReceiver receiver;


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(WALLPAPER_ACTION);
            receiver = new VideoReceiver();
            registerReceiver(receiver, intentFilter);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            surfaceInitialized = true;
            Log.e(TAG, "onSurfaceCreated " + holder);
            super.onSurfaceCreated(holder);
            wrapper.initMediaSource(holder);
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            Log.e(TAG, "onSurfaceRedrawNeeded  " + holder);

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.e(TAG, "onSurfaceChanged  " + holder);

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG, "onSurfaceDestroyed  " + holder);
            super.onSurfaceDestroyed(holder);

        }

        @Override
        public void onDestroy() {
            Log.e(TAG, "onDestroy");
            super.onDestroy();
            unregisterReceiver(receiver);
        }

        public class VideoReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                if (action.equals(WALLPAPER_ACTION)) {
                    float volume = intent.getFloatExtra(ACTION_VOICE, 0.0f);
                    MediaPlayerWrapper.getInstant().volume(volume);
                }
            }
        }
    }
}
