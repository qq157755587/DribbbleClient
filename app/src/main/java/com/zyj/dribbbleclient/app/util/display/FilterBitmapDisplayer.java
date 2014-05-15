package com.zyj.dribbbleclient.app.util.display;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import jp.co.cyberagent.android.gpuimage.GPUImageColorMatrixFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class FilterBitmapDisplayer implements BitmapDisplayer {

    private static final long STEP = 100;
    private final int durationMillis;
    private long startMillis;
    private GPUImageView imageView;
    private GPUImageColorMatrixFilter colorMatrixFilter;
    private Handler handler;

    public FilterBitmapDisplayer(int durationMillis) {
        this.durationMillis = durationMillis;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);

        imageView = (GPUImageView) imageAware.getWrappedView();
        GPUImageFilter filter = imageView.getFilter();
        if (filter instanceof GPUImageColorMatrixFilter) {
            colorMatrixFilter = (GPUImageColorMatrixFilter) filter;
            startMillis = System.currentTimeMillis();
            handler = new Handler();
            handler.postDelayed(runnable, STEP);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            animate();
        }
    };

    private void animate() {

        long duration = System.currentTimeMillis() - startMillis;
        if (duration < durationMillis) {
            float intensity = 1f - duration / (float) durationMillis;
            colorMatrixFilter.setIntensity(intensity);
            handler.postDelayed(runnable, STEP);
        } else {
            colorMatrixFilter.setIntensity(0f);
        }
        imageView.requestRender();
    }
}
