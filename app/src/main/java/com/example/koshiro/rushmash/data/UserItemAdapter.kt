package com.example.koshiro.rushmash.data

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.koshiro.rushmash.R
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.main.activity_main.*

class UserItemAdapter(data: OrderedRealmCollection<UserItem>?) : RealmBaseAdapter<UserItem>(data) {
    inner class ViewHolder(cell: View) {
        // TODO: 項目のid入れる
        val name = cell.findViewById<TextView>(R.id.todo_textview)
        val duration = cell.findViewById<TextView>(R.id.time_textview)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                Log.i("UserItemAdapter.getView", "inflate view")
                view = inflater.inflate(R.layout.list_items, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run {
            val userItem = get(position)
            Log.i("UserItemAdapter.adapterData.run", "Get item: " + userItem.name)
            viewHolder.name.text = userItem.name
            val duration = userItem.duration.toString() + "分"
            viewHolder.duration.text = duration
//            if (position == 0) {
//                convertView?.setBackgroundColor(Color.rgb(127, 127, 255))
//            }
        }
        return view
    }
}