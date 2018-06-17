package com.example.koshiro.rushmash.schedule

import kotlin.math.abs
import com.example.koshiro.rushmash.data.*
import io.realm.Realm
import io.realm.kotlin.where

class Scheduler {

    private lateinit var realm: Realm

    fun optimizeSchedule(remineTime: Int): Boolean {
        realm = Realm.getDefaultInstance()
        val results = realm.where<UserItem>()
                .equalTo("isDeleted", false)
                .findAll()

        var durationSum: Int=0
        results.map { durationSum += it.duration }

        if (remineTime >= durationSum) {
            attachCategory()
            return true
        }

        // 削除する時間を計算
        var forDeleteTime = durationSum - remineTime

        // PriorityがLowの中から削除
        var lows = realm.where<UserItem>()
                .equalTo("priority", 2.toInt())
                .and()
                .equalTo("isDeleted", false)
                .findAll()
        var lowsSorted = lows.sortedBy { abs(forDeleteTime - it.duration) }
        for (l in lowsSorted) {
            forDeleteTime -= l.duration
            realm.executeTransaction{
                l.isDeleted = true
            }
            if (forDeleteTime <= 0){
                attachCategory(true)
                return true
            }
        }

        // PriorityがMidの中から削除
        var mids = realm.where<UserItem>()
                .equalTo("priority", 1.toInt())
                .and()
                .equalTo("isDeleted", false)
                .findAll()
        var midsSorted = mids.sortedBy { abs(forDeleteTime - it.duration) }
        for (m in midsSorted) {
            forDeleteTime -= m.duration
            realm.executeTransaction{
                m.isDeleted = true
            }
            if (forDeleteTime <= 0) {
                attachCategory(true)
                return true
            }
        }

        // PriorityがHighは削除しないので，forDeleteTimeが1以上であれば諦める
        return false
    }

    fun attachCategory(isOverslept: Boolean = false) {
        realm = Realm.getDefaultInstance()
        val results = realm.where<UserItem>()
                .equalTo("isDeleted", false)
                .findAll()
        var durationSum: Int=0
        results.map{ durationSum += it.duration }
        var categoryTime: Int = durationSum / 3

        var tmp: Int = 0
        var cat: Int = 0
        if (isOverslept){
            cat = 1
            categoryTime = durationSum / 2
        }
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