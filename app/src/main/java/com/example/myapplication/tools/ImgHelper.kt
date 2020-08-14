package com.example.myapplication.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.R

object ImgHelper {

    //Normal Loads Customized
    fun loadNormalImg(
        ctx: Context?,
        url: String?,
        ivHolder: ImageView?) {
        //drawable int = 0 代表空的drawable
        val nullDrawableID = R.drawable.crop_image_menu_rotate_right
        Log.e("Peter","IMAGECHECK")
        this.loadImage(ctx, url, ivHolder, nullDrawableID, DiskCacheStrategy.ALL, false)
    }

    //Normal Load
    private fun loadImage(
        ctx: Context?,
        url: String?,
        ivHolder: ImageView?,
        errorImg: Int,
        cacheStrategy: DiskCacheStrategy,
        skipCache: Boolean) {

        if (this.isDestroy(ctx)) {
            return
        }
        Log.e("Peter","IMAGECHECK?   "+url)

        Glide.with(ctx!!).load(url)
            .diskCacheStrategy(cacheStrategy)
            .centerCrop()
            .placeholder(errorImg)
            .error(errorImg)
            .skipMemoryCache(skipCache)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Peter", "IMG err    $e")
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Peter","IMG err   ")
                    return false
                }

            })
            .into(ivHolder!!)
    }

    //Tools
    //判断Activity是否Destroy
    @SuppressLint("ObsoleteSdkInt")
     fun isDestroy(ctx: Context?): Boolean {
        if (ctx == null) {
            return true
        }
        return if (ctx is Activity) {
            val activity = ctx as Activity?
            activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
        } else {
            false
        }
    }
}