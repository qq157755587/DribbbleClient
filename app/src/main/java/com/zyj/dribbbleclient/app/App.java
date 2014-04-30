package com.zyj.dribbbleclient.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;
import com.squareup.okhttp.apache.OkApacheClient;
import com.zyj.dribbbleclient.app.util.Util;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Config ImageLoader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .imageDownloader(new HttpClientImageDownloader(this, new OkApacheClient()))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        // Get device width and height
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Util.DEVICE_WIDTH = size.x;
        Util.DEVICE_HEIGHT = size.y;
    }
}
