package com.example.koshiro.rushmash

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_finish.*


class FinishActivity : AppCompatActivity() {

    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        setSupportActionBar(finish_toolbar)

        finish_view.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            intent.putExtra("DEMO_SECOND_TIME", "8:15")
            startActivity(intent)
        }

        player = MediaPlayer.create(this, R.raw.clear)
        player.start()
    }
}
