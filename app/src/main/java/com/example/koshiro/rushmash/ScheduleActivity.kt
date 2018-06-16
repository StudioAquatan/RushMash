package com.example.koshiro.rushmash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        setSupportActionBar(schedule_toolbar)

        stop_music_Button.setOnClickListener {
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
        }
    }
}
