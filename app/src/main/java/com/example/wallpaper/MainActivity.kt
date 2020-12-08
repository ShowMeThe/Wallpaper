package com.example.wallpaper

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.photo.wallpaper.ICallBack
import com.photo.wallpaper.WallpaperClient
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val callback = object : ICallBack {
        override fun onPaperStart() {
            Log.e("22222", "start")
        }

        override fun onPaperChange() {
            Log.e("22222", "change")
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val manager = WallpaperClient.getClient()
        manager.doInitFirst(this.applicationContext)

        btn.setOnClickListener {
            manager.setLiveImageWallpaper(
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.wallpaper
                ), callback
            )
        }

        btn2.setOnClickListener {
            val assetFile = assets.open("WeChat_20201014194055.mp4").buffered()
            val fileDir =
                File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator + "video")
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val videoFile = File(fileDir.path + File.separator + "1.mp4")
            val writer = videoFile.outputStream().buffered()
            writer.write(assetFile.readBytes())
            manager.setLiveVideoWallpaper(videoFile,callback)
        }

        btn3.setOnClickListener {
            val assetFile = assets.open("2.mp4").buffered()
            val fileDir =
                File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator + "video")
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val videoFile = File(fileDir.path + File.separator + "2.mp4")
            val writer = videoFile.outputStream().buffered()
            writer.write(assetFile.readBytes())
            manager.setLiveVideoWallpaper(videoFile,callback)
        }

        btn4.setOnClickListener {
            try {
                WallpaperManager.getInstance(this).clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("22222222","$data")
    }

}