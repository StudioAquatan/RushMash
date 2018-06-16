package com.example.koshiro.rushmash

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlin.concurrent.thread

class ScheduleActivity : AppCompatActivity() {

    private var player: MediaPlayer? = null
    private val firstPeriodMsec: Long = 10 * 1000     //序盤の時間(msec) なんらかの方法で取得
    private val secondPeriodMsec: Long = 10 * 1000     //中盤の時間(msec)
    private val thirdTimeMsec: Long = 10 * 1000      //終盤の時間(msec)
    private var playerCreateFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        setSupportActionBar(schedule_toolbar)

        stop_music_Button.setOnClickListener {
            audioStop()
            playerCreateFlag = false
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
        }
        playScheduleMusic(firstPeriodMsec, secondPeriodMsec, thirdTimeMsec)
    }
    private fun playScheduleMusic(firstMsec: Long, secondMsec: Long, thirdMsec: Long) {
        Log.d("test", "playScheduleMusic start")
        thread {
            if (!firstMsec.equals(0)) {     //序盤の音楽
                audioPlay(1)
                Log.d("timer", "first timer start! $firstMsec[msec]")
                if(playerCreateFlag) Thread.sleep(firstMsec)
                Log.d("timer", "first timer ended!")
                //一旦曲の再生を止める
                audioStop()
            }
            if (!secondMsec.equals(0)) {    //中盤の音楽
                audioPlay(2)
                Log.d("timer", "second timer start! $secondMsec[msec]")
                if(playerCreateFlag)  Thread.sleep(secondMsec)
                Log.d("timer", "second timer ended!")
                //一旦曲の再生を止める
                audioStop()
            }
            if (!thirdMsec.equals(0)) {     //終盤の音楽
                audioPlay(3)
                Log.d("timer", "third timer start! $thirdMsec[msec]")
                if(playerCreateFlag)  Thread.sleep(thirdMsec)
                Log.d("timer", "third timer ended!")
                //一旦曲の再生を止める
                audioStop()
            }
            if(playerCreateFlag) {
                val intent = Intent(this, FinishActivity::class.java)
                startActivity(intent)
            }
        }
        Log.d("test", "playScheduleMusic end")
    }
    private fun audioSetup(musicId: Int) {
        Log.d("musicid", musicId.toString())
        when (musicId) {
            1 -> {  //序盤の曲
                player = MediaPlayer.create(this, R.raw.first)
                playerCreateFlag = true
            }
            2 -> {  //中盤の曲
                if (playerCreateFlag) player = MediaPlayer.create(this, R.raw.middle)
            }
            3 -> {  //終盤の曲
                if (playerCreateFlag) player = MediaPlayer.create(this, R.raw.last)
            }
            else -> {
                //エラーのときの処理
            }
        }
        player?.isLooping = true
        volumeControlStream = AudioManager.STREAM_MUSIC
    }
    fun audioPlay(musicId: Int) {
        if (player == null) {
            audioSetup(musicId)     //この関数処理後にplayerCreateFlagが立ってればplayerはnullじゃなくなる
        }
        if (player != null) {
            player!!.start()
        }
    }
    fun audioStop() {
        if (player != null) {
            player.apply {
                this!!.stop()
                reset()
                release()
            }
            player = null
        }
    }
}
