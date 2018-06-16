package com.example.koshiro.rushmash.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

enum class Priority {
    HIGH,
    MID,
    LOW
}

open class UserItem: RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String = ""
    var duration: Int = 0
    var priority: Int = Priority.MID.ordinal
    var order = 0
    var category = 0
    var isDeleted = false
}



