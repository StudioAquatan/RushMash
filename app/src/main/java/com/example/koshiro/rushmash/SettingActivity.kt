package com.example.koshiro.rushmash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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

        val itemId = intent?.getLongExtra("item_id", -1L)
        if (itemId != -1L) {
            val userItem = realm.where<UserItem>().equalTo("id", itemId).findFirst()!!
            val todolist = resources.getStringArray(R.array.todolist)
            plan_spinner.setSelection(todolist.indexOf(userItem.name))
            priority_spinner.setSelection(userItem.priority)
            time_editview.setText(userItem.duration.toString(), TextView.BufferType.EDITABLE)
            delete_button.visibility = View.VISIBLE
        } else {
            delete_button.visibility = View.INVISIBLE
        }

        save_button.setOnClickListener {
            when (itemId) {
                -1L -> {
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
                else -> {
                    realm.executeTransaction {
                        val item = realm.where<UserItem>().equalTo("id", itemId).findFirst()!!
                        item.name = plan_spinner.selectedItem as String
                        item.priority = priority_spinner.selectedItemPosition
                        item.duration = Integer.parseInt(time_editview.text.toString())
                    }
                    alert("更新しました") {
                        yesButton { finish() }
                    }.show()
                }
            }
        }
        delete_button.setOnClickListener {
            realm.executeTransaction {
                realm.where<UserItem>().equalTo("id", itemId).findFirst()!!.deleteFromRealm()
            }
            alert("削除しました") {
                yesButton { finish() }
            }.show()
        }
        cancel_button.setOnClickListener {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
