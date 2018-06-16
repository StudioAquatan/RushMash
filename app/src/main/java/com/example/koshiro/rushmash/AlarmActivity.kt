package com.example.koshiro.rushmash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm.*

import com.example.koshiro.rushmash.schedule.*

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        if(intent.getStringExtra("DEMO_SECOND_TIME") != null) {
            time_textview.text = intent.getStringExtra("DEMO_SECOND_TIME")
        }

        stop_alarm_button.setOnClickListener {
            /*
            optimizeScheduleの引数には残りの時間(分)をIntで渡す
            最適化に成功したらtrue，失敗したらfalseを返す
             */
            val scheduler = Scheduler()
            scheduler.optimizeSchedule(30)
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }
    }


}
