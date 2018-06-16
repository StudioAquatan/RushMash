package com.example.koshiro.rushmash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.koshiro.rushmash.data.UserItem
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class SettingActivity : AppCompatActivity() {
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        realm = Realm.getDefaultInstance()

        save_button.setOnClickListener {
            realm.executeTransaction {
                val maxId = realm.where<UserItem>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1
                val item = realm.createObject<UserItem>(nextId)
                item.name = plan_spinner.selectedItem as String
                item.priority = priority_spinner.selectedItemPosition
                item.duration = Integer.parseInt(time_editview.text.toString())
            }
            alert("保存しました") {
                    yesButton { finish() }
            }.show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
