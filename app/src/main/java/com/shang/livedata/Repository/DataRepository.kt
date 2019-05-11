package com.shang.livedata.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Firebase.FirebaseDao
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao
import com.shang.livedata.Room.RoomDatabase
import com.shang.livedata.Room.SettingEntity

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

    //EventDao
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
        return eventDao.getDayToDataEntity(calendarDay)
    }


    //FirebaseDao
    fun getFirebaseLiveData(): FirebaseLiveData {
        return firebaseDao.getFirebaseData()
    }

    fun pushFirebase(dataEntity: DataEntity) {
        firebaseDao.push(dataEntity)
    }

    fun updateFirebase(dataEntity: DataEntity) {
        firebaseDao.update(dataEntity)
    }

    fun deleteFirebase(dataEntity: DataEntity) {
        firebaseDao.delete(dataEntity)
    }


    //Setting
    fun insertSetting(settingEntity: SettingEntity) {
        eventDao.insertSetting(settingEntity)
    }

    fun getSetting(): SettingEntity {
        return eventDao.getSetting()
    }

    fun updateSetting(settingEntity: SettingEntity){
        eventDao.updateSetting(settingEntity)
    }


}