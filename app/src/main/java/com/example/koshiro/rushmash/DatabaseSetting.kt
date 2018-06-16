package com.example.koshiro.rushmash

import android.app.Application
import com.example.koshiro.rushmash.data.UserMigration
import io.realm.Realm
import io.realm.RealmConfiguration

class DatabaseSetting : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .migration(UserMigration()).build()

        Realm.setDefaultConfiguration(config)
    }
}

