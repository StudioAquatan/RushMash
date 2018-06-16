package com.example.koshiro.rushmash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_finish.*


class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setSupportActionBar(finish_toolbar)

        finish_view.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            intent.putExtra("DEMO_SECOND_TIME_DISPLAY", "8:00")
            intent.putExtra("DEMO_SECOND_TIME_DEMOTIME", 30)
            startActivity(intent)
        }

    }



}
