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

    public static boolean created = false;

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private  MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();

    public void volume(@FloatRange(from = 0.0,to = 1.0) float volume){
        Intent intent = new Intent();
        intent.setAction(WALLPAPER_ACTION);
        intent.putExtra(ACTION_VOICE,volume);
        getApplicationContext().sendBroadcast(intent);
    }



    public void setLiveVideoWallpaper(Context context,File videoPath) {
        wrapper.setPlayingFile(videoPath);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.VIDEO);
        createActivity(context);
    }

    public void setImageWallPaper(Context context, InputStream imageFile) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context);
    }

    public void setImageWallPaper(Context context, Bitmap imageFile) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context);
    }

    public void setImageWallPaper(Context context,File imageFile) {
        wrapper.setPhotoInput(imageFile);
        wrapper.setMediaType(MediaPlayerWrapper.MediaType.PHOTO);
        createActivity(context);
    }


    private void createActivity(Context context){
        Log.e(TAG,"createActivity " + created + " holder " + wrapper.holder);
        if(!created){
            created = true;
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new ComponentName(context,LiveWallPaper.class));
            context.startActivity(intent);
        }else {
            wrapper.initMediaSource(wrapper.holder);
        }
    }

    private class WallpaperEngine extends Engine{
        private MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();
        private VideoReceiver receiver;


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(WALLPAPER_ACTION);
            receiver = new VideoReceiver();
            registerReceiver(receiver,intentFilter);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.e(TAG,"onSurfaceCreated " + holder);
            super.onSurfaceCreated(holder);
            wrapper.initMediaSource(holder);
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            Log.e(TAG,"onSurfaceRedrawNeeded  " + holder);

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.e(TAG,"onSurfaceChanged  " + holder);

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG,"onSurfaceDestroyed  " + holder);
            super.onSurfaceDestroyed(holder);

        }

        @Override
        public void onDestroy() {
            Log.e(TAG,"onDestroy");
            super.onDestroy();
            unregisterReceiver(receiver);
            //created = false;
        }

        public class VideoReceiver extends BroadcastReceiver{

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                assert action != null;
                if(action.equals(WALLPAPER_ACTION)){
                    float volume = intent.getFloatExtra(ACTION_VOICE,0.0f);
                    MediaPlayerWrapper.getInstant().volume(volume);
                }
            }
        }
    }
}