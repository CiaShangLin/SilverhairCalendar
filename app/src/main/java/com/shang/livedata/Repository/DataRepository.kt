package com.shang.livedata.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.FirebaseDao
import com.shang.livedata.FirebaseLiveData
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao
import com.shang.livedata.Room.RoomDatabase

class DataRepository {

    private lateinit var myRoomDatabase: RoomDatabase
    private lateinit var eventDao: EventDao
    private lateinit var firebaseDao: FirebaseDao

    constructor(context: Context) {
        myRoomDatabase = Room.databaseBuilder(context, RoomDatabase::class.java, RoomDatabase.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
        eventDao = myRoomDatabase.getEventDao()
        firebaseDao = FirebaseDao(eventDao)
    }

    fun getAllData(): LiveData<MutableList<DataEntity>> {
        return eventDao.getMyDataEntity()
    }

    fun insert(myDataEntity: DataEntity) {
        eventDao.insert(myDataEntity)
    }

    fun delete(myDataEntity: DataEntity) {
        eventDao.delete(myDataEntity)
    }

    fun update(myDataEntity: DataEntity) {
        eventDao.update(myDataEntity)
    }

    fun getDay(calendarDay: CalendarDay): LiveData<MutableList<DataEntity>> {
        return eventDao.getDay(calendarDay)
    }

    fun getFirebaseLiveData(): FirebaseLiveData {
        return firebaseDao.getFirebaseData()
    }

    fun pushFirebase(dataEntity: DataEntity){
        firebaseDao.push(dataEntity)
    }

    fun updateFirebase(dataEntity: DataEntity){
        firebaseDao.update(dataEntity)
    }

    fun deleteFirebase(dataEntity: DataEntity){
        firebaseDao.delete(dataEntity)
    }


}