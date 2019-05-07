package com.shang.livedata.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SettingEntity.TABLE_NAME)
class SettingEntity {

    companion object {
        const val TABLE_NAME = "SettingTable"
    }

    var name: String = ""

    @PrimaryKey
    var firebaseCode: String = ""

}