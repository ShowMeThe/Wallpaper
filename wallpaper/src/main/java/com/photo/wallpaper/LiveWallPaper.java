package com.photo.wallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.FloatRange;

import java.io.File;
import java.io.IOException;

import static com.photo.wallpaper.Const.ACTION_VOICE;
import static com.photo.wallpaper.Const.WALLPAPER_ACTION;

public class LiveWallPaper extends WallpaperService {

    private static final String TAG = "LiveWallPaper";

    private boolean created = false;

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    public void volume(@FloatRange(from = 0.0,to = 1.0) float volume){
        Intent intent = new Intent();
        intent.setAction(WALLPAPER_ACTION);
        intent.putExtra(ACTION_VOICE,volume);
        getApplicationContext().sendBroadcast(intent);
    }

    public void setWallPaper(Context context,File videoPath) throws IOException {
        MediaPlayerWrapper wrapper = MediaPlayerWrapper.getInstant();
        wrapper.setPlayingFile(videoPath);
        if(!created){
            created = true;
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,new ComponentName(context,LiveWallPaper.class));
            context.startActivity(intent);
        }else {
            wrapper.destroy();
            wrapper.initMediaSource(wrapper.holder);
            wrapper.start();
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
            Log.e(TAG,"onVisibilityChanged");

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.e(TAG,"onSurfaceCreated");
            super.onSurfaceCreated(holder);
            wrapper.initMediaSource(holder);
            wrapper.start();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG,"onSurfaceDestroyed");
            super.onSurfaceDestroyed(holder);
            //wrapper.destroy();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(receiver);
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
