@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.example.myapplication.tools

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewConfiguration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.myapplication.MyApp
import okhttp3.*
import okio.BufferedSink
import okio.Sink
import okio.buffer
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

import okhttp3.Call

import okhttp3.Callback

import okhttp3.OkHttpClient

import okhttp3.Request

import okhttp3.Response


import okio.*



object Tools {
    const val IMAGE_UPLOAD_LIMIT = 1000000

    interface AudioDownListener{
        fun success()
    }

    fun toast(ctx: Context?, msg: String?) {
        (ctx as Activity).runOnUiThread {
            try {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus == null) {
            return
        }
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    fun logout(){
        PrefHelper.setApiHeader("")
        PrefHelper.setUserID("")
        PrefHelper.setChatToken("")
        PrefHelper.setChatId("")
        PrefHelper.setChatLable("")
    }

    //    public static boolean localBigHeadExist() {
    //        File file = new File(getLocalBigHeadImagePath() );
    //        return file.exists();
    //    }
    fun getLocalSavedImagePath(): String? {
        return Environment.getExternalStorageDirectory().path + File.separatorChar + Environment.DIRECTORY_DCIM + File.separatorChar + "crop.png"
    }
    fun getLocalSavedAudioPath(): String? {
        return Environment.getExternalStorageDirectory().path + File.separatorChar + Environment.DIRECTORY_DCIM + File.separatorChar
    }

    fun dealCropImage(): File {
        var imgFile = File(getLocalSavedImagePath())
        Log.e("Peter","dealCropImage:out   ${imgFile.length()}")

        while (imgFile.length() > IMAGE_UPLOAD_LIMIT){
            Log.e("Peter","dealCropImage:   ${imgFile.length()}")
            compressCropImage()
            imgFile = File(getLocalSavedImagePath())
        }
        Log.e("Peter","dealCropImage:out2   ${imgFile.length()}")

        return imgFile
    }

    fun compressCropImage() {
        val local: String? = getLocalSavedImagePath()
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        // 此时返回bm为空
        var bitmap = BitmapFactory.decodeFile(local, newOpts)
        newOpts.inJustDecodeBounds = false
        // 设置缩放比例-+
        newOpts.inSampleSize = 2

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(local, newOpts)
        saveCropImage(bitmap)
    }

    fun saveCropImage(bmp: Bitmap) {
        val local: String? = Tools.getLocalSavedImagePath()
        try {
            FileOutputStream(local).use { out ->
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveCropImage(sourceUri: Uri) {
        val sourceFilename = sourceUri.path
        val destinationFilename: String? = Tools.getLocalSavedImagePath()
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(FileInputStream(sourceFilename))
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Peter", "saveCropImage: " + e.message)
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {//0919020739
                e.printStackTrace()
            }
            Log.e("peter", "saveCropImage: $destinationFilename")
        }
    }

    fun deleteCropImage(){
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val where = MediaStore.Images.Media.DATA + "='" + getLocalSavedImagePath() + "'"
        val mContentResolver: ContentResolver = (MyApp.get() as Context).contentResolver
        mContentResolver.delete(uri, where, null)
    }


    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (hasNavBar(context)) {
            val res: Resources = context.resources
            val resourceId: Int = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag
            val sNavBarOverride: String? = navBarOverride
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            hasNav
        } else { // fallback
            !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    private val navBarOverride: String?
        @SuppressLint("PrivateApi")
        get() {
            var sNavBarOverride: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m =
                        c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (e: Throwable) {
                }
            }
            return sNavBarOverride
        }

    fun pxToDp(Px: Int): Int {
        return (Px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        var bitmap: Bitmap?
//        Thread(Runnable {
//            bitmap = try {
//                val url = URL(src)
//                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                connection.setDoInput(true)
//                connection.connect()
//                val input: InputStream = connection.inputStream
//                BitmapFactory.decodeStream(input)
//            } catch (e: IOException) {
//                // Log exception
//                null
//            }
//
//        }).start()

        bitmap = try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }

        return bitmap

    }


    fun downloadFile3(fileUrl: String, callback: AudioDownListener) {
        //下载路径，如果路径无效了，可换成你的下载路径

        val path= Environment.getExternalStorageDirectory().path + File.separatorChar + Environment.DIRECTORY_DCIM + File.separatorChar + "crop.png"

        var url = "http://c.qijingonline.com/test.mkv"
        url = fileUrl
        val startTime = System.currentTimeMillis()
        Log.i("DOWNLOAD", "startTime=$startTime")
        val request = Request.Builder().url(url).addHeader("X-Auth-Token", PrefHelper.chatToken!!)
            .addHeader("X-User-Id", PrefHelper.chatId!!).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 下载失败
                e.printStackTrace()
                Log.i("DOWNLOAD", "download failed")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var sink: Sink? = null
                var bufferedSink: BufferedSink? = null
                try {
                    val mSDCardPath =Environment.getExternalStorageDirectory().path + File.separatorChar + Environment.DIRECTORY_DCIM + File.separatorChar
                        //Environment.getExternalStorageDirectory().absolutePath
                    val dest = File(mSDCardPath, url.substring(url.lastIndexOf("/") + 1))
//                    val dest = File(getLocalSavedAudioPath())

                    sink = dest.sink()
                    bufferedSink = sink.buffer()
                    bufferedSink.writeAll(response.body!!.source())
                    bufferedSink.close()
                    callback.success()
                    Log.i("DOWNLOAD", "download success")
                    Log.i(
                        "DOWNLOAD",
                        "totalTime=" + (System.currentTimeMillis() - startTime)
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.i("DOWNLOAD", "download failed")
                } finally {
                    bufferedSink?.close()
                }
            }
        })
    }
}