package com.example.myapplication.custom_view

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import com.example.myapplication.R
import com.example.myapplication.`interface`.AudioInterface
import com.example.myapplication.tools.AudioRecordHelper
import kotlinx.android.synthetic.main.layout_audio_player.view.*

class AudioPlayerLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), AudioInterface {
    private var audioPlayer: MediaPlayer
    private var seekBarThread: Thread
    private var isSeeking: Boolean
    private var AudioInstance: AudioRecordHelper
    private var AudioUrl: String

    init {
        Log.e("Peter","AudioRecordHelper AudioPlayerLayout init")
        AudioUrl = ""
        AudioInstance = AudioRecordHelper
        View.inflate(getContext(), R.layout.layout_audio_player, this)
        tv_audio_time.text = "100"

//        AudioInstance.setCallBack(this)
        audioPlayer = AudioInstance.getPlayer()
        isSeeking = false

        sb_progress_seek_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                Log.e("Peter","setAudioSource    onProgressChanged  ")
                if(isSeeking && AudioUrl == AudioInstance.getCurrentUrl()) {
                    Log.e("Peter","setAudioSource    onProgressChanged  seekTo    "+sb_progress_seek_bar.progress)

                    AudioInstance.getPlayer().seekTo(sb_progress_seek_bar.progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.e("Peter","setAudioSource    onStartTrackingTouch  ")
                if(AudioUrl == AudioInstance.getCurrentUrl()) {
                    isSeeking = true
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.e("Peter","setAudioSource    onStopTrackingTouch  ")
                if(AudioUrl == AudioInstance.getCurrentUrl()) {
                    isSeeking = false
                    AudioInstance.getPlayer().start()
                }
            }
        })

        tv_audio_play_button.setOnClickListener {
            playAudio(AudioUrl)
        }

        seekBarThread = Thread(Runnable {
            while (AudioUrl == AudioInstance.getCurrentUrl()) {
                Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    ININ")

//                Log.e("Peter","setAudioSource    seekBarThread  ")
                Thread.sleep(500)
                if (!isSeeking && AudioUrl == AudioInstance.getCurrentUrl()) {
                    Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    "+AudioUrl)
                    sb_progress_seek_bar.progress = AudioInstance.getPlayer().currentPosition
                }
            }
            Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    OUTOUT1   $AudioUrl")
            Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    OUTOUT2   ${AudioInstance.getCurrentUrl()}")


        })
//        seekBarThread.start()
    }

    fun setAudioSource(url: String){
        Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    SETSet   $url")
        AudioUrl = url
    }

    private fun playAudio(audioUrl: String) {
        Log.e("Peter","seekBarThread isAlive   "+seekBarThread.isAlive)

        if (!seekBarThread.isAlive){
            if(seekBarThread == null){
                Log.e("Peter","seekBarThread == null   "+seekBarThread.state)
            } else {
                Log.e("Peter","seekBarThread != null   "+seekBarThread.state)
                if(seekBarThread.state == Thread.State.TERMINATED){
                    seekBarThread = Thread(Runnable {
                        while (AudioUrl == AudioInstance.getCurrentUrl()) {
                            Thread.sleep(500)
                            if (!isSeeking && AudioUrl == AudioInstance.getCurrentUrl()) {
                                Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    "+AudioUrl)
                                sb_progress_seek_bar.progress = AudioInstance.getPlayer().currentPosition
                            }
                        }
                        Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    OUTOUT11   $AudioUrl")
                        Log.e("Peter","setAudioSource    seekBarThread  !isSeeking    OUTOUT22   ${AudioInstance.getCurrentUrl()}")
                    })
                }

//                seekBarThread.start()
            }
        }
        AudioInstance.setCallBack(this)
        AudioInstance.play(audioUrl)
    }

    fun stopAudio(){
        AudioInstance.stopRecord()
    }

    override fun setAudioDuration(duration: Int) {
        Log.e("Peter","setAudioSource    setAudioDuration  ")
        if (AudioUrl == AudioInstance.getCurrentUrl()) {
            sb_progress_seek_bar.max = duration
            tv_audio_time.text = duration.toString()
        }


    }

    override fun setCurrentPosition(position: Int) {
        Log.e("Peter","setAudioSource    setCurrentPosition  ")
        if (AudioUrl == AudioInstance.getCurrentUrl()) {
            Log.e("Peter","setAudioSource    setCurrentPosition  IN   $AudioUrl")

            sb_progress_seek_bar.progress = position
        }
    }

    override fun onAttachedToWindow() {
        Log.e("Peter","setAudioSource  QQ  onAttachedToWindow  ")
        if (AudioUrl == AudioInstance.getCurrentUrl()) {
            AudioInstance.setCallBack(this)
            sb_progress_seek_bar.max = AudioInstance.getPlayer().duration
            tv_audio_time.text = AudioInstance.getPlayer().duration.toString()
        }

        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        Log.e("Peter","setAudioSource  QQ  onDetachedFromWindow  ")
        AudioUrl = ""
//        AudioInstance.removeCallBack()
        sb_progress_seek_bar.progress = 0
        tv_audio_time.text = "100"

//        AudioInstance.getPlayer().reset()
//        isSeeking = true
        super.onDetachedFromWindow()
    }
}
