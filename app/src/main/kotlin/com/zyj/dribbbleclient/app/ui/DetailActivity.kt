package com.zyj.dribbbleclient.app.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.ImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.zyj.dribbbleclient.app.R
import com.zyj.dribbbleclient.app.model.Shot

/**
 * Created by zhaoyuanjie on 15/2/12.
 */
public class DetailActivity : AppCompatActivity() {
    private var image : ImageView? = null;
    private var id = 0;
    private var url : String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initData()
        initView()
        setupListener()
    }

    fun initData() {
        id = intent.getIntExtra("id", 0)
        url = intent.getStringExtra("url")
    }

    fun initView() {
        image = findViewById(R.id.image) as ImageView?
        ImageLoader.getInstance().displayImage(url, image)
    }

    fun setupListener() {
        image?.setOnClickListener { onBackPressed() }
    }
}