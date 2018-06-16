package com.example.koshiro.rushmash.schedule

import kotlin.math.abs
import com.example.koshiro.rushmash.data.*
import io.realm.Realm
import io.realm.kotlin.where

//var mockData: List<UserItem> = listOf(
//        UserItem(
//                id = 1,
//                name = "朝食",
//                duration = 15,
//                priority = 2,
//                category = 0),
//        UserItem(
//                id = 4,
//                name = "歯磨き",
//                duration = 5,
//                priority = 1,
//                category = 0),
//        UserItem(
//                id = 2,
//                name = "シャワー",
//                duration = 20,
//                priority = 2,
//                category = 0),
//        UserItem(
//                id = 3,
//                name = "着替え",
//                duration = 5,
//                priority = 0,
//                category = 0),
//        UserItem(
//                id = 5,
//                name = "荷物用意",
//                duration = 5,
//                priority = 0,
//                category = 0),
//        UserItem(
//                id = 6,
//                name = "身支度",
//                duration = 2,
//                priority = 1,
//                category = 0),
//        UserItem(
//                id = 7,
//                name = "トイレ",
//                duration = 3,
//                priority = 2,
//                category = 0)
//)

class Scheduler {

    private lateinit var realm: Realm

    fun optimizeSchedule(remineTime: Int): Boolean {
        realm = Realm.getDefaultInstance()
        val results = realm.where<UserItem>().findAll()
        var durationSum: Int=0
        for (r in results){
            durationSum += r.duration
        }
        if (remineTime >= durationSum) {
            attachCategory()
            return true
        }

        // 削除する時間を計算
        var forDeleteTime = durationSum - remineTime

        // PriorityがLowの中から削除
        var lows = realm.where<UserItem>().equalTo("priority", 2.toInt()).findAll()
        var lowsSorted = lows.sortedBy { abs(forDeleteTime - it.duration) }
        for (l in lowsSorted) {
            forDeleteTime -= l.duration
            realm.executeTransaction{
                l.deleteFromRealm()
            }
            if (forDeleteTime <= 0){
                attachCategory()
                return true
            }
        }

        // PriorityがMidの中から削除
        var mids = realm.where<UserItem>().equalTo("priority", 1.toInt()).findAll()
        var midsSorted = mids.sortedBy { abs(forDeleteTime - it.duration) }
        for (m in midsSorted) {
            forDeleteTime -= m.duration
            realm.executeTransaction{
                m.deleteFromRealm()
            }
            if (forDeleteTime <= 0) {
                attachCategory()
                return true
            }
        }

        // PriorityがHighは削除しないので，forDeleteTimeが1以上であれば諦める
        return false
    }

    fun attachCategory() {
        val results = realm.where<UserItem>().findAll()
        var durationSum: Int=0
        for (r in results){
            durationSum += r.duration
        }
        var categoryTime: Int = durationSum / 3

        var tmp: Int = 0
        var cat: Int = 0
        for (r in results) {

            // categoryの値は2から先が無いので全部2
            if (cat == 2) {
                realm.executeTransaction{
                    r.category = cat
                }
                continue
            }

            tmp += r.duration
            if (tmp >= categoryTime) {
                // 足す前
                var diff1 = categoryTime - (tmp - r.duration)
                // 足した後
                var diff2 = tmp - categoryTime
                if (diff1 <= diff2) {
                    cat++
                    realm.executeTransaction{
                        r.category = cat
                    }
                    tmp = r.duration
                } else {
                    realm.executeTransaction{
                        r.category = cat
                    }
                    cat++
                    tmp = 0
                }
                continue
            }
            realm.executeTransaction {
                r.category = cat
            }
        }
        val all = realm.where<UserItem>().findAll()
        for (a in all){
            println(a)
        }
    }
}