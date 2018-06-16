package com.example.koshiro.rushmash.schedule

import kotlin.math.abs

enum class MockPriority {
    High, Mid, Low
}

data class MockToDo (
        var id: Int,
        var name: String,
        var duration: Int,
        var mockPriority: MockPriority,
        var category: Int
)

class Scheduler {

    fun optimizeSchedule(remineTime: Int, schedules: MutableList<MockToDo>): MutableList<MockToDo>{
        /*
            削除優先
         */
        var durationSum: Int = 0
        schedules.map { durationSum += it.duration }
        println(durationSum)
        if (remineTime >= durationSum) {
            println("通常の予定通りです")
            return schedules
        }

        println("残り時間が足りないので予定を削ります")
        // 削除する時間を計算
        var forDeleteTime = durationSum - remineTime

        // PriorityがLowの中から削除
        var lows = schedules.filter { it.mockPriority == MockPriority.Low }
        var lowsSorted = lows.sortedBy { abs(forDeleteTime-it.duration) }
        print("low sorted : ")
        println(lowsSorted)
        for (l in lowsSorted){
            schedules.dropWhile { it.id == l.id }
            forDeleteTime -= l.duration
            if (forDeleteTime <= 0){
                return schedules
            }
        }

        // PriorityがMidの中から削除
        var mids = schedules.filter { it.mockPriority == MockPriority.Mid }
        var midsSorted = mids.sortedBy { abs(forDeleteTime-it.duration) }
        print("mid sorted : ")
        println(midsSorted)
        for (m in midsSorted){
            schedules.dropWhile { it.id == m.id }
            forDeleteTime -= m.duration
            if (forDeleteTime <= 0){
                return schedules
            }
        }

        // PriorityがHighは削除しないので，forDeleteTimeが1以上であれば諦める
        return mutableListOf()
    }

    fun attachCategory(schedules: MutableList<MockToDo>): MutableList<MockToDo>{
        var durationSum: Int = 0
        schedules.map { durationSum += it.duration }
        var categoryTime: Int = durationSum / 3

        var tmp: Int = 0
        var cat = 0
        for (sch in schedules){
            tmp += sch.duration
            if (tmp >= categoryTime){
                var diff1 = categoryTime - (tmp-sch.duration)
                var diff2 = tmp - categoryTime
                if (diff1 <= diff2){
                    cat++
                    sch.category = cat
                    tmp = sch.duration
                }else{
                    sch.category = cat
                    cat++
                    tmp = 0
                }
            }
            sch.category = cat
        }

        return schedules
    }
}