package com.zyj.dribbbleclient.app.util.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class GLSurfaceViewAware implements ImageAware {

    private Reference<GPUImageView> imageViewRef;
    private boolean checkActualViewSize;

    public GLSurfaceViewAware(GPUImageView surfaceView) {
        this(surfaceView, true);
    }

    public GLSurfaceViewAware(GPUImageView imageView, boolean checkActualViewSize) {
        imageViewRef = new WeakReference<GPUImageView>(imageView);
        this.checkActualViewSize = checkActualViewSize;
        imageView.setFilter(new GPUImageSepiaFilter());
    }

    @Override
    public int getWidth() {
        GPUImageView surfaceView = imageViewRef.get();
        if (surfaceView != null) {
            final ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
            int width = 0;
            if (checkActualViewSize && params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = surfaceView.getWidth(); // Get actual image width
            }
            if (width <= 0 && params != null) width = params.width; // Get layout width parameter
//            if (width <= 0) width = getImageViewFieldValue(surfaceView, "mWidth"); // Check width parameter
            return width;
        }
        return 0;
    }

    @Override
    public int getHeight() {
        GPUImageView surfaceView = imageViewRef.get();
        if (surfaceView != null) {
            final ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
            int height = 0;
            if (checkActualViewSize && params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = surfaceView.getHeight(); // Get actual image height
            }
            if (height <= 0 && params != null) height = params.height; // Get layout height parameter
//            if (height <= 0) height = getImageViewFieldValue(surfaceView, "mHeight"); // Check height parameter
            return height;
        }
        return 0;
    }

    @Override
    public ViewScaleType getScaleType() {
        return ViewScaleType.CROP;
    }

    @Override
    public View getWrappedView() {
        return imageViewRef.get();
    }

    @Override
    public boolean isCollected() {
        return imageViewRef.get() == null;
    }

    @Override
    public int getId() {
        GPUImageView surfaceView = imageViewRef.get();
        return surfaceView == null? super.hashCode() : surfaceView.hashCode();
    }

    @Override
    public boolean setImageDrawable(Drawable drawable) {
        return false;
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        GPUImageView imageView = imageViewRef.get();
        if (imageView != null) {
            imageView.setImage(bitmap);
            return true;
        }
        return false;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = GPUImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            Log.e("GLSurfaceViewAware", e.getMessage());
        }
        return value;
    }
}
