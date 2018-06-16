package com.example.koshiro.rushmash

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.koshiro.rushmash.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground)

        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }

        addListButton.setOnClickListener {

        }

        setAlarmButton.setOnClickListener {

        }
    }

}
