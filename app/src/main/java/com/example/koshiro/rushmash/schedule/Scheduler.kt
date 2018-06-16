package com.example.koshiro.rushmash.schedule

import kotlin.math.abs

enum class MockPriority {
    High, Mid, Low
}

data class MockToDo(
        var id: Int,
        var name: String,
        var duration: Int,
        var priority: Int,
        var category: Int
)

var mockData: MutableList<MockToDo> = mutableListOf(
        MockToDo(
                id = 1,
                name = "朝食",
                duration = 15,
                priority = 2,
                category = 0),
        MockToDo(
                id = 2,
                name = "シャワー",
                duration = 20,
                priority = 1,
                category = 0),
        MockToDo(
                id = 3,
                name = "着替え",
                duration = 5,
                priority = 0,
                category = 0),
        MockToDo(
                id = 4,
                name = "歯磨き",
                duration = 5,
                priority = 1,
                category = 0),
        MockToDo(
                id = 5,
                name = "荷物用意",
                duration = 5,
                priority = 0,
                category = 0),
        MockToDo(
                id = 6,
                name = "身支度",
                duration = 2,
                priority = 1,
                category = 0),
        MockToDo(
                id = 7,
                name = "トイレ",
                duration = 3,
                priority = 2,
                category = 0)
)

class Scheduler {

    fun optimizeSchedule(remineTime: Int, schedules: MutableList<MockToDo>): MutableList<MockToDo> {
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
        var lows = schedules.filter { it.priority == 2 }
        var lowsSorted = lows.sortedBy { abs(forDeleteTime - it.duration) }
        print("low sorted : ")
        println(lowsSorted)
        for (l in lowsSorted) {
            schedules.dropWhile { it.id == l.id }
            forDeleteTime -= l.duration
            if (forDeleteTime <= 0) {
                return schedules
            }
        }

        // PriorityがMidの中から削除
        var mids = schedules.filter { it.priority == 1 }
        var midsSorted = mids.sortedBy { abs(forDeleteTime - it.duration) }
        print("mid sorted : ")
        println(midsSorted)
        for (m in midsSorted) {
            schedules.dropWhile { it.id == m.id }
            forDeleteTime -= m.duration
            if (forDeleteTime <= 0) {
                return schedules
            }
        }

        // PriorityがHighは削除しないので，forDeleteTimeが1以上であれば諦める
        return mutableListOf()
    }

    fun attachCategory(schedules: MutableList<MockToDo>): MutableList<MockToDo> {
        var durationSum: Int = 0
        schedules.map { durationSum += it.duration }
        var categoryTime: Int = durationSum / 3

        var tmp: Int = 0
        var cat = 0
        for (sch in schedules) {
            tmp += sch.duration
            if (tmp >= categoryTime) {
                var diff1 = categoryTime - (tmp - sch.duration)
                var diff2 = tmp - categoryTime
                if (diff1 <= diff2) {
                    cat++
                    sch.category = cat
                    tmp = sch.duration
                } else {
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