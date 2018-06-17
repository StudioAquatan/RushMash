package com.example.koshiro.rushmash

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_alarm.*

import com.example.koshiro.rushmash.schedule.*

class AlarmActivity : AppCompatActivity() {

    companion object {
        private var qikokuFlag: Boolean = false
    }
    private var player: MediaPlayer? = null
    private var late_time: Int = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarm_toolbar.title = "おはようございます"
        setContentView(R.layout.activity_alarm)

        if (intent.getStringExtra("DEMO_SECOND_TIME_DISPLAY") != null) {
            time_textview.text = intent.getStringExtra("DEMO_SECOND_TIME_DISPLAY")
        }
        if (intent.getIntExtra("DEMO_SECOND_TIME_DEMOTIME", 0) != 0) {
            late_time = intent.getIntExtra("DEMO_SECOND_TIME_DEMOTIME", 0)
        }

        stop_alarm_button.setOnClickListener {
            // 再生を止める
            audioStop()
            /*
            optimizeScheduleの引数には残りの時間(分)をIntで渡す
            最適化に成功したらtrue，失敗したらfalseを返す
             */
            val scheduler = Scheduler()
            scheduler.optimizeSchedule(late_time)
            Log.d("late_time", "late_time is $late_time")
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        if (!qikokuFlag) {
            player = MediaPlayer.create(this, R.raw.alarm)
        } else {
            player = MediaPlayer.create(this, R.raw.alarm_qikoku)
        }
        player!!.isLooping = true
        player!!.start()
        Log.d("music","music start")
        Log.d("flag","qikokuFlag is $qikokuFlag")
        qikokuFlag = !qikokuFlag
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

    override fun onDestroy() {
        super.onDestroy()
        audioStop()
    }
}
