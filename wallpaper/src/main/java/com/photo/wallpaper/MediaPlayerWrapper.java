package com.photo.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class MediaPlayerWrapper implements IWrapper {


    enum MediaType {
        PHOTO, VIDEO
    }


    private MediaType mediaType;

    private MediaPlayerWrapper() {
        surfaceView = new WeakReference<>(new SurfaceView(WallpaperClient.getClient().getContext().get()));
    }

    private MediaPlayer player;

    SurfaceHolder holder;

    private static MediaPlayerWrapper instant;


    public static MediaPlayerWrapper getInstant() {
        if (instant == null) {
            synchronized (MediaPlayerWrapper.class) {
                if (instant == null) {
                    instant = new MediaPlayerWrapper();
                }
            }
        }
        return instant;
    }

    private File playingFile;

    private InputStream photoInput;

    private Bitmap liveBitmap;

    private WeakReference<SurfaceView> surfaceView;

    private Paint paint;

    public void setPlayingFile(File playingFile) {
        this.playingFile = playingFile;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setPhotoInput(Bitmap photoInput) {
        liveBitmap = photoInput;
    }

    public void setPhotoInput(File photoInput) {
        liveBitmap = BitmapFactory.decodeFile(photoInput.getAbsolutePath());
    }

    public void setPhotoInput(InputStream photoInput) {
        liveBitmap = BitmapFactory.decodeStream(photoInput);
    }

    public boolean isPlaying() {
        if (player == null) {
            return false;
        } else {
            return player.isPlaying();
        }
    }

    public void volume(float volume) {
        player.setVolume(volume, volume);
    }

    @Override
    public void initMediaSource(SurfaceHolder holder) {
        this.holder = holder;
        if (player != null && player.isPlaying()) {
            player.reset();
            player.release();
            player = null;
        }
        if (playingFile != null && mediaType == MediaType.VIDEO) {
            player = new MediaPlayer();

            player.setSurface(this.holder.getSurface());
            try {
                player.setDataSource(playingFile.getPath());
                player.setLooping(true);
                player.setVolume(0f, 0f);
                player.prepareAsync();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        player.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mediaType == MediaType.PHOTO) {
            if (liveBitmap != null) {
                Rect rect = new Rect(0, 0, WallpaperClient.getClient().getSCREEN_WIDTH(), WallpaperClient.getClient().getSCREEN_HEIGHT());
                Canvas canvas = holder.lockCanvas(rect);
                if (paint == null) {
                    paint = new Paint();
                }
                canvas.drawBitmap(liveBitmap, new Rect(0, 0, liveBitmap.getWidth(), liveBitmap.getHeight()), rect, paint);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
