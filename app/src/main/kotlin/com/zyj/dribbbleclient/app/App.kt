package com.zyj.dribbbleclient.app

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Point
import android.view.WindowManager
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.zyj.dribbbleclient.app.util.Device
import se.emilsjolander.sprinkles.Migration
import se.emilsjolander.sprinkles.Sprinkles

public class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Config ImageLoader
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(FadeInBitmapDisplayer(500))
                .resetViewBeforeLoading(true)
                .build()
        val config = ImageLoaderConfiguration.Builder(applicationContext)
                .defaultDisplayImageOptions(defaultOptions)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build()
        ImageLoader.getInstance().init(config)

        // Get device width and height
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        Device.width = size.x
        Device.height = size.y

        // Config Sprinkles
        val sprinkles = Sprinkles.init(applicationContext)
        sprinkles.addMigration(object : Migration() {
            override fun doMigration(sqLiteDatabase: SQLiteDatabase) {
                sqLiteDatabase.execSQL(
                        """CREATE TABLE shots (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        type TEXT,
                        body TEXT)"""
                )
            }
        })
    }
}
