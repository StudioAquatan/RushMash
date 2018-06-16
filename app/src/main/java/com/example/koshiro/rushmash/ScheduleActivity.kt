package com.example.koshiro.rushmash

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.koshiro.rushmash.data.UserItem
import com.example.koshiro.rushmash.data.UserItemAdapter
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlin.concurrent.thread
import io.realm.Realm
import io.realm.kotlin.where

class ScheduleActivity : AppCompatActivity() {

    lateinit var realm: Realm

    private var player: MediaPlayer? = null
    private var firstPeriodMsec: Long = 10 * 1000     //序盤の時間(msec) なんらかの方法で取得
    private var middlePeriodMsec: Long = 10 * 1000     //中盤の時間(msec)
    private var lastPeriodMsec: Long = 10 * 1000      //終盤の時間(msec)
    private var playerCreateFlag = true
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        schedule_toolbar.setTitle("8:30までのスケジュール")
        setSupportActionBar(schedule_toolbar)

        realm = Realm.getDefaultInstance()
        val scheduledItems = realm.where<UserItem>().equalTo("isDeleted", false).findAll()
        val userItemAdapter = UserItemAdapter(scheduledItems)
        current_listview.adapter = userItemAdapter

        stop_music_Button.setOnClickListener {
            audioStop()
            playerCreateFlag = false
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
        }

        realm = Realm.getDefaultInstance()

        val firsts = realm.where<UserItem>()
                .equalTo("isDeleted", false)
                .and()
                .equalTo("category", 0.toInt())
                .findAll()
        firstPeriodMsec = 0
        firsts.map { firstPeriodMsec += it.duration*60*1000 }

        val middles = realm.where<UserItem>()
                .equalTo("isDeleted", false)
                .and()
                .equalTo("category", 1.toInt())
                .findAll()
        middlePeriodMsec = 0
        middles.map { middlePeriodMsec += it.duration*60*1000 }

        val lasts = realm.where<UserItem>()
                .equalTo("isDeleted", false)
                .and()
                .equalTo("category", 2.toInt())
                .findAll()
        lastPeriodMsec = 0
        lasts.map { lastPeriodMsec += it.duration*60*1000 }

        println("first: $firstPeriodMsec ms")
        println("middle: $middlePeriodMsec ms")
        println("last: $lastPeriodMsec ms")

        playScheduleMusic(firstPeriodMsec, middlePeriodMsec, lastPeriodMsec)
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
