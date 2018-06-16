package com.example.koshiro.rushmash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.koshiro.rushmash.data.UserItem
import com.example.koshiro.rushmash.data.UserItemAdapter
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        main_toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground)

        realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.where<UserItem>().findAll().deleteAllFromRealm()
        }

        val results = realm.where<UserItem>().findAll()
        original_listview.adapter = UserItemAdapter(results)

        main_toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }

        add_list_button.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        start_button.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
