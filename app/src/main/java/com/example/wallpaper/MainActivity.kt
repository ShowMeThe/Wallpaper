package com.example.wallpaper

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
            manager.downloadLiveWallpaper("http://resource.unbing.cn/configs/c94db97e-4439-4ebc-8906-5191da3ea6ee.mp4"){
                manager.setLiveWallpaper(it)
            }
        }


        btn2.setOnClickListener {
            manager.setLiveWallpaper(File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path
                    + File.separator + "Wallpaper_video" + File.separator+ "WeChat_20201014194055.mp4"))
        }




    }
}