@file:Suppress("UNREACHABLE_CODE")

package com.example.myapplication.tools

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.format.DateFormat
import android.util.Log
import cn.trinea.android.common.util.FileUtils
import com.example.myapplication.`interface`.AudioInterface
import java.io.File
import java.io.IOException
import java.util.*


object AudioRecordHelper{

    private var audioRecord: MediaRecorder?
    private var audioPlayer: MediaPlayer?
    private var TIME_COUNT: Int
    private var audioSaveDir: String
    private var timeCount: Int
    private var isRecording: Boolean
    private val myHandler: Handler
    private var countThread: CountTread
    private var currentPositionThread: SeekBarThread
    private var callbacks: AudioInterface?
    private var hadDataFirstSet: Boolean
    private lateinit var mContext: Context
    private lateinit var fileName: String
    private lateinit var filePath: String

    class CountTread : Thread() {
        @Volatile  var exit: Boolean = false

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

    class SeekBarThread : Thread() {
        @Volatile  var exit: Boolean = false

        fun exit(){
            exit = true
        }

        override fun run() {
            while (!exit) {
                if (audioPlayer?.isPlaying!!){
                    sleep(500)
                    audioPlayer?.currentPosition?.let { callbacks?.setCurrentPosition(it) }
                }
            }
        }
    }

    init {
        Log.e("Peter","AudioRecordHelper init")
        hadDataFirstSet = false

        val externalStorageDir = Environment.getExternalStorageDirectory().toString()
        filePath = ""
        countThread = CountTread()
        currentPositionThread = SeekBarThread()
        currentPositionThread.start()
        callbacks = object: AudioInterface{
            override fun setAudioDuration(duration: Int) {
                Log.e("Peter","setAudioDuration default")
            }

            override fun setCurrentPosition(position: Int) {
                Log.e("Peter","setCurrentPosition default")
            }
        }

        timeCount = 0
        isRecording = false
        audioRecord = MediaRecorder()
        audioPlayer = MediaPlayer()
        TIME_COUNT = 0x101
        audioSaveDir= externalStorageDir + File.separator + "Download" + File.separator

        myHandler = @SuppressLint("HandlerLeak")
        object : Handler(Looper.getMainLooper()){
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

    fun init(){

    }
    fun setCallBack(param: AudioInterface){
        Log.e("Peter","setAudioSource    setCallBack  ")

        callbacks = param
    }

    fun removeCallBack(){
        Log.e("Peter","setAudioSource    setCallBack  ")
        callbacks = null
    }

    fun setContext(context: Context){
        mContext = context
    }

    fun get(): MediaRecorder? {
        return audioRecord
    }

    fun getPlayer(): MediaPlayer {
        return audioPlayer!!
    }

    fun getCurrentUrl(): String{
        return filePath
    }

    fun startRecord(){
        isRecording = true
        Log.e("Peter","startRecord")
        try {
            audioRecord?.setAudioSource(MediaRecorder.AudioSource.MIC)
            audioRecord?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            audioRecord?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)).toString()+".m4a"
            if (!FileUtils.isFolderExist(FileUtils.getFolderName(audioSaveDir))) {
                FileUtils.makeFolders(audioSaveDir);
            }
            filePath = audioSaveDir+ fileName
            audioRecord?.setOutputFile(filePath)
            audioRecord?.prepare()
            audioRecord?.start()

            countThread.start()

        } catch (e:IllegalStateException){
            LogUtil.e("call startAmr(File mRecAudioFile) failed!",""+e.message)

        } catch (e: IOException){
            LogUtil.e("call startAmr(File mRecAudioFile) failed!",""+e.message)
        }
    }

    fun stopRecord(): String{
        isRecording = false
        countThread.exit()
        return try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            filePath
    //            filePath = ""
        } catch (e: RuntimeException) {
            audioRecord?.reset()
            audioRecord?.release()
            audioRecord = null
            val file = File(filePath)
            if (file.exists()) file.delete()
            ""
    //            filePath = ""
        }
    }

    fun play(url: String){
        if(url != filePath){
            filePath = url
            audioPlayer?.stop()
        }

        when {
            url.contains("https://") -> {
                Log.e("Peter","audioPlayer play")

                playRecord()
            }
            url.contains(Environment.getExternalStorageDirectory().toString()) -> {
                Log.e("Peter","audioPlayer play1")

                playLocalRecord()
            }
            else -> {
                Log.e("Peter","audioPlayer play2")

                playLocalRecord()

            }
        }
    }

    fun playLocalRecord() {
        try {
            //如果正在播放，然后在播放其他文件就直接崩溃了
            if(audioPlayer?.isPlaying!!){
                Log.e("Peter","audioPlayer return")
                return;
            }
            if(hadDataFirstSet){
                audioPlayer?.reset()
            } else {
                hadDataFirstSet = true
            }
            audioPlayer?.setDataSource(filePath)
            audioPlayer?.prepare()
            audioPlayer?.setOnPreparedListener(OnPreparedListener { mp ->
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放1"+audioPlayer?.duration)
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放2"+mp.duration)
                callbacks?.setAudioDuration(mp.duration)
                mp.start()
            })
            audioPlayer?.setOnCompletionListener { mp ->
                Log.e("Peter","audioPlayer in5  reset")

            }
            Log.e("Peter","audioPlayer in6   $filePath")

        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun playRecord() {
        try {

            //如果正在播放，然后在播放其他文件就直接崩溃了
            if(audioPlayer?.isPlaying!!){
                Log.e("Peter","audioPlayer return")
                return;
            }
            Log.e("Peter","audioPlayer ininin   $filePath" )
            val uri1: Uri = if(filePath!=null){
                Uri.parse(filePath)
            }else{
                Uri.parse(filePath)
            }
            val headers: HashMap<String, String> = HashMap()
            headers["X-Auth-Token"] = PrefHelper.getChatToken()
            headers["X-User-Id"] = PrefHelper.getChatId()


//            this.filePath = filepath2
            //设置数据源
            if(hadDataFirstSet){
                audioPlayer?.reset()
            } else {
                hadDataFirstSet = true
            }

            audioPlayer?.setOnErrorListener { p0, p1, p2 ->
                Log.e("Peter","audioPlayer setOnErrorListener p1   $p1    p2     $p2")
                return@setOnErrorListener true
            }
            audioPlayer?.setOnPreparedListener(OnPreparedListener { mp ->
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放1"+audioPlayer?.duration)
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放2"+mp.duration)
                callbacks?.setAudioDuration(mp.duration)
                mp.start()
            })
            audioPlayer?.setDataSource(mContext, uri1, headers)
            audioPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            audioPlayer?.prepareAsync()


            audioPlayer?.setOnCompletionListener { mp ->
                Log.e("Peter","audioPlayer in5  reset")

                //播放完毕再重置一下状态，下次播放可以再次使用
//                mp.reset()
            }
            Log.e("Peter","audioPlayer in6   $filePath")

        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun countTime(){
        Log.e("Peter","正在錄音1")

        if (isRecording) {

            Log.e("Peter","正在錄音")
            timeCount++
            val msg: Message = Message.obtain()
            msg.what = TIME_COUNT
            msg.obj = timeCount
            myHandler.sendMessage(msg)
        } else {

            Log.e("Peter","結束錄音")
            timeCount = 0
            val msg: Message = Message.obtain()
            msg.what = TIME_COUNT
            msg.obj = timeCount
            myHandler.sendMessage(msg)
        }
    }


    fun setAudioSource(filepath2: String){
        try {
            //如果正在播放，然后在播放其他文件就直接崩溃了
            if(audioPlayer?.isPlaying!!){
                Log.e("Peter","audioPlayer return")
                return;
            }
            Log.e("Peter","audioPlayer in   $filepath2" )
            val uri1: Uri = if(filepath2!=null){
                Uri.parse(filepath2)
            }else{
                Uri.parse(filepath2)
            }
            val headers: HashMap<String, String> = HashMap()
            headers["X-Auth-Token"] = PrefHelper.getChatToken()
            headers["X-User-Id"] = PrefHelper.getChatId()


//            this.filePath = filepath2
            //设置数据源
            audioPlayer?.setDataSource(mContext, uri1, headers)
            audioPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            audioPlayer?.prepareAsync()
            audioPlayer?.setOnPreparedListener(OnPreparedListener { mp ->
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放1"+audioPlayer?.duration)
                Log.e("Peter ", "audioPlayer   setOnPreparedListener  开始播放2"+mp.duration)
                callbacks?.setAudioDuration(mp.duration)
//                mp.start()
            })
        } catch (e: IOException){
            e.printStackTrace()

        }
    }
}
