package com.example.myapplication.activity

import android.R.attr.targetActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.`interface`.ImageLoaderModle
import com.example.myapplication.tools.ImgHelper
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_full_screen_image.*


class FullScreenImageActivity : AppCompatActivity() {

    private var imgUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_full_screen_image)
        getIntentData()
        init()

    }

    private fun getIntentData(){
        val b = intent?.extras
        imgUrl = b?.getString("imgUrl")!!
    }

    private fun init(){

        Log.e("Peter", "DialogFullScreenImage    $imgUrl")
        tv_close.setOnClickListener(onClick)
        ActivityCompat.postponeEnterTransition(this);

        ImgHelper.setImageLoaderModle(object : ImageLoaderModle{
            override fun onSuccess() {
                ActivityCompat.startPostponedEnterTransition(this@FullScreenImageActivity);
            }

            override fun OnError() {
                ActivityCompat.startPostponedEnterTransition(this@FullScreenImageActivity);
            }

        })
        ImgHelper.loadOriginalImage(this, imgUrl, iv_photo)

//        iv_photo.setImage(ImageSource.uri(message.imageUrl.toString()))
    }

    private var onClick : View.OnClickListener ?= View.OnClickListener {
        when (it.id) {
            R.id.tv_close -> onBackPressed()
        }
    }
}
