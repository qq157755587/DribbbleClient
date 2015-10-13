package com.zyj.dribbbleclient.app.model

/**
 * Created by zhaoyuanjie on 15/10/13.
 */
data class Images(val hidpi: String? = null,
                  val normal: String? = null,
                  val teaser: String? = null)

data class Shot(val id: Int,
                val title: String?,
                val description: String?,
                val width: Int,
                val height: Int,
                val images: Images = Images(),
                val views_count: Int,
                val likes_count: Int,
                val comments_count: Int)