package com.example.wallpaper

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.photo.wallpaper.LivePaperCallBack
import com.photo.wallpaper.WallpaperClient
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val manager = WallpaperClient.getClient()
        manager.doInitFirst(this)

        btn.setOnClickListener {
            manager.setLiveImageWallpaper(this,
                BitmapFactory.decodeResource(
                    resources,
                    R.mipmap.wallpaper
                )
            )
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
            manager.setLiveVideoWallpaper(this,videoFile)
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
            manager.setLiveVideoWallpaper(this,videoFile)
        }

        btn4.setOnClickListener {
            try {
                WallpaperManager.getInstance(this).clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        manager.setLivePaperCallBack(object : LivePaperCallBack{
            override fun onSuccess() {
                Log.e("22222222","onSuccess")
            }

            override fun onFailed() {
                Log.e("22222222","onFailed")
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        WallpaperClient.onActivityResult(requestCode, resultCode, data)
    }

}