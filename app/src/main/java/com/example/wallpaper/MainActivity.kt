package com.example.wallpaper

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.LinearLayout
import com.photo.wallpaper.WallpaperClient
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val manager = WallpaperClient.getClient()
        manager.doInitFirst(this)

        btn.setOnClickListener {
            manager.setLiveImageWallpaper(BitmapFactory.decodeResource(resources,R.mipmap.wallpaper))
        }

        btn2.setOnClickListener {
            val assetFile = assets.open("WeChat_20201014194055.mp4").buffered()
            val fileDir = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator + "video")
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            val videoFile = File(fileDir.path + File.separator + "1.mp4")
            val writer = videoFile.outputStream().buffered()
            writer.write(assetFile.readBytes())
            manager.setLiveVideoWallpaper(videoFile)
        }

        btn3.setOnClickListener {
            val assetFile = assets.open("2.mp4").buffered()
            val fileDir = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator + "video")
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            val videoFile = File(fileDir.path + File.separator + "2.mp4")
            val writer = videoFile.outputStream().buffered()
            writer.write(assetFile.readBytes())
            manager.setLiveVideoWallpaper(videoFile)
        }

    }
}