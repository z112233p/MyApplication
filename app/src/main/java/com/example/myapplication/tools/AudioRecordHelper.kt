package com.example.myapplication.tools

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.text.format.DateFormat
import android.util.Log
import cn.trinea.android.common.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

object AudioRecordHelper {

    private var audioRecord: MediaRecorder?
    private var TIME_COUNT: Int
    private var audioSaveDir: String
    private var timeCount: Int
    private var isRecording: Boolean
    private val myHandler: Handler
    private var countThread: CountTread
    private lateinit var fileName: String
    private lateinit var filePath: String

    class CountTread : Thread() {
        @Volatile public var exit: Boolean = false

        fun exit(){
            exit = true
        }

        override fun run() {
            while (!exit) {
                sleep(1000)
                countTime()
            }
        }
    }

    init {
//        countThread = Thread(Runnable {
//            while (true) {
//                Thread.sleep(1000)
//                countTime()
//
//            }
//        })
        countThread = CountTread()

        val externalStorageDir =
            Environment.getExternalStorageDirectory().toString()
        val testfile =
            File(externalStorageDir + File.separator + "Download" + File.separator + "test.txt")
        timeCount = 0
        isRecording = false
        audioRecord = MediaRecorder()
        TIME_COUNT = 0x101
        audioSaveDir= externalStorageDir + File.separator + "Download" + File.separator

        myHandler = @SuppressLint("HandlerLeak")
        object : Handler(){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    TIME_COUNT ->{
                        val count =  msg.obj
                        LogUtil.e("peter", "count == $count");
                    }

                }
            }
        }

    }

    fun get(): MediaRecorder? {
        return audioRecord
    }

    fun startRecord(){
        isRecording = true
        Log.e("Peter","startRecord")
        try {
            audioRecord?.setAudioSource(MediaRecorder.AudioSource.MIC)
            audioRecord?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            audioRecord?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)).toString()+".m4a";
            if (!FileUtils.isFolderExist(FileUtils.getFolderName(audioSaveDir))) {
                FileUtils.makeFolders(audioSaveDir);
            }
            filePath = audioSaveDir+ fileName
            audioRecord?.setOutputFile(filePath)
            audioRecord?.prepare()
            audioRecord?.start()

            countThread.start()

        } catch (e:IllegalStateException){
            LogUtil.e("call startAmr(File mRecAudioFile) failed!",""+e.message);

        } catch (e: IOException){
            LogUtil.e("call startAmr(File mRecAudioFile) failed!",""+e.message);
        }
    }

    fun stopRecord(){
        isRecording = false
        countThread.exit()
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            filePath = ""
        } catch (e: RuntimeException) {
            audioRecord?.reset()
            audioRecord?.release()
            audioRecord = null
            val file = File(filePath)
            if (file.exists()) file.delete()
            filePath = ""
        }
    }

    fun countTime(){
        Log.e("Peter","正在錄音1")

        while (isRecording) {
            Log.e("Peter","正在錄音")
            timeCount
            val msg: Message = Message.obtain()
            msg.what = TIME_COUNT
            msg.obj = timeCount
            myHandler.sendMessage(msg)
            isRecording = false
//            try {
////                timeThread.sleep(1000)
////                GlobalScope.launch(context = Dispatchers.Main){
////                    delay(1000)
////                    countTime()
////                }
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
        }
        Log.e("Peter","結束錄音")

        timeCount = 0
        val msg: Message = Message.obtain()
        msg.what = TIME_COUNT
        msg.obj = timeCount
        myHandler.sendMessage(msg)
    }

}
