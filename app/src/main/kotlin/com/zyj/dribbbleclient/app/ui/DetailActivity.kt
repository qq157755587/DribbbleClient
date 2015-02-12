package com.zyj.dribbbleclient.app.ui

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import com.zyj.dribbbleclient.app.R
import android.widget.ImageView
import com.zyj.dribbbleclient.app.model.Shot
import com.nostra13.universalimageloader.core.ImageLoader

/**
 * Created by zhaoyuanjie on 15/2/12.
 */
public class DetailActivity : ActionBarActivity() {
    private var image : ImageView? = null;
    private var shot : Shot? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initData()
        initView()
    }

    fun initData() {
        shot = getIntent().getParcelableExtra("shot")
    }

    fun initView() {
        image = findViewById(R.id.image) as ImageView?
        ImageLoader.getInstance().displayImage(shot?.image_url, image)
    }
}