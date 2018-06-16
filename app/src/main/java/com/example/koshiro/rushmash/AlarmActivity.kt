package com.example.koshiro.rushmash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        if(intent.getStringExtra("DEMO_SECOND_TIME") != null) {
            time_textview.text = intent.getStringExtra("DEMO_SECOND_TIME")
        }

        stop_alarm_button.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }
    }


}
