package com.example.koshiro.rushmash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.koshiro.rushmash.data.UserItem
import com.example.koshiro.rushmash.data.UserItemAdapter
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        main_toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground)

        realm = Realm.getDefaultInstance()
//        realm.executeTransaction {
//            realm.where<UserItem>().findAll().deleteAllFromRealm()
//        }
        realm.executeTransaction {
            val maxId = realm.where<UserItem>().max("id")
            val nextId = (maxId?.toLong() ?: 0L) + 1
            val item = realm.createObject<UserItem>(nextId)
            item.name = "歯磨き"
            item.priority = 0
            item.duration = 1
            item.category = 1
        }

        val results = realm.where<UserItem>().findAll()
        original_listview.adapter = UserItemAdapter(results)

        original_listview.setOnItemClickListener {
            parent, view, position, id ->
                val userItem = parent.getItemAtPosition(position) as UserItem
                startActivity<SettingActivity>(
                        "item_id" to userItem.id
                )
        }

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
