package com.shang.livedata.Firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.shang.livedata.Room.DataEntity

data class FirebaseData(var data: DataSnapshot, var type: Int) {
    companion object {
        val TYPE_INSERT = 1
        val TYPE_DELETE = 2
        val TYPE_UPDATE = 3
        val TYPE_CANCEL = 4
        val TYPE_MOVE = 5
    }

    //Firebase轉換成DataEntity
    fun dataSnapshotToDataEntity(dataSnapshot: DataSnapshot): DataEntity {
        var dataEntity: DataEntity? = dataSnapshot.getValue(DataEntity::class.java)
        if (dataEntity != null) {
            dataEntity.calendarDay = dataEntity.stringToCalendarDay(dataEntity.calendarDayString) //轉換CalendarDay
        }
        return dataEntity!!
    }

}