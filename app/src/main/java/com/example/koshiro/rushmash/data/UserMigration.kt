package com.example.koshiro.rushmash.data

import io.realm.*

class UserMigration: RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var v = oldVersion
        val schema = realm.schema
        if (v == 0.toLong()) {
//            open class UserItem: RealmObject() {
//                @PrimaryKey
//                var id: Long = 0
//                var name: String = ""
//                var duration: Int = 0
//                var priority: Int = Priority.MID.ordinal
//                var order = 0
//                var category = 0
//                var isDeleted = false
            schema.create("UserItem")
                    .addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String::class.java, FieldAttribute.REQUIRED).transform { obj: DynamicRealmObject? ->
                        obj?.setString("name", "")
                    }
                    .addField("duration", Int::class.java).transform { obj: DynamicRealmObject? ->
                        obj?.setInt("duration", 0)
                    }
                    .addField("priority", Int::class.java).transform { obj: DynamicRealmObject? ->
                        obj?.setInt("priority", 0)
                    }
                    .addField("order", Int::class.java).transform { obj: DynamicRealmObject? ->
                        obj?.setInt("order", 0)
                    }
                    .addField("category", Int::class.java).transform { obj: DynamicRealmObject? ->
                        obj?.setInt("category", 0)
                    }
        }
        if (v == 1.toLong()) {
            schema.create("UserItem")
                .addField("isDeleted", Boolean::class.java).transform {
                    obj: DynamicRealmObject? ->
                    obj?.setBoolean("isDeleted", false)
                }
            v += 1
        }
    }
}

