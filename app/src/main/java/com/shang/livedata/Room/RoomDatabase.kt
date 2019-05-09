package com.shang.livedata.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DataEntity::class,SettingEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class RoomDatabase : RoomDatabase() {
    companion object {
        val DATABASE_NAME = "DATABASE_NAME"
    }

    abstract fun getEventDao(): EventDao
}