package com.photo.wallpaper;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

public class MediaPlayerWrapper implements IWrapper {

    private MediaPlayerWrapper(){}

    private MediaPlayer player;

    SurfaceHolder holder;

    private static MediaPlayerWrapper instant;

    public static MediaPlayerWrapper getInstant(){
        if(instant == null){
            synchronized (MediaPlayerWrapper.class){
                if(instant == null){
                    instant = new MediaPlayerWrapper();
                }
            }
        }
        return instant;
    }

    private File playingFile;

    public void setPlayingFile(File playingFile) {
        this.playingFile = playingFile;
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void volume(float volume){
        player.setVolume(volume,volume);
    }

    @Override
    public void initMediaSource(SurfaceHolder holder) {
        this.holder = holder;
        if(playingFile!=null){
            player = new MediaPlayer();
            player.setSurface(holder.getSurface());
            try {
                player.setDataSource(playingFile.getPath());
                player.setLooping(true);
                player.setVolume(0f, 0f);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new IllegalStateException("Media File is Null");
        }
    }

    @Override
    public void start() {
        if(player!=null && !player.isPlaying()){
            player.start();
        }

    }

    @Override
    public void pause() {
        if(player!=null && player.isPlaying()){
            player.pause();
        }
    }

    @Override
    public void destroy() {
        if (player != null){
            player.release();
            player = null;
        }
    }
}
