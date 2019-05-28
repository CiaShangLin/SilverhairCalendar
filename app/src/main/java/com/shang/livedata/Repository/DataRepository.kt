package com.shang.livedata.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Firebase.FirebaseData
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao
import com.shang.livedata.Room.RoomDatabase
import com.shang.livedata.Room.SettingEntity

class DataRepository {

    private lateinit var myRoomDatabase: RoomDatabase
    private lateinit var eventDao: EventDao

    constructor(context: Context) {
        myRoomDatabase = Room.databaseBuilder(context, RoomDatabase::class.java, RoomDatabase.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
        eventDao = myRoomDatabase.getEventDao()
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

    fun checkFirebaseCode(firebaseCode: String): Boolean {
        return eventDao.checkFirebaseCode(firebaseCode)
    }

    fun getFirebaseCodeToId(firebaseCode: String): Int {
        return eventDao.getFirebaseCodeToId(firebaseCode)
    }


    //Setting
    fun insertSetting(settingEntity: SettingEntity) {
        eventDao.insertSetting(settingEntity)
    }

    fun getSetting(): SettingEntity {
        return eventDao.getSetting()
    }

    fun updateSetting(settingEntity: SettingEntity) {
        eventDao.updateSetting(settingEntity)
    }

    //Firebase
    fun firebaseDao(firebaseData: FirebaseData) {
        when(firebaseData.type){
            FirebaseData.TYPE_INSERT->{
                var dataEntity=firebaseData.dataSnapshotToDataEntity(firebaseData.data)
                //檢查firebaseCode是因為每次onActive他都會跑一次
                if(!eventDao.checkFirebaseCode(dataEntity.firebaseCode)){
                    eventDao.insert(dataEntity)
                }
            }
            FirebaseData.TYPE_DELETE->{
                var dataEntity = firebaseData.dataSnapshotToDataEntity(firebaseData.data)
                if (dataEntity != null) {
                    dataEntity.id = eventDao.getFirebaseCodeToId(dataEntity.firebaseCode)
                    eventDao.delete(dataEntity)
                }
            }
            FirebaseData.TYPE_UPDATE->{
                var dataEntity = firebaseData.dataSnapshotToDataEntity(firebaseData.data)
                if (dataEntity != null) {
                    dataEntity.id = eventDao.getFirebaseCodeToId(dataEntity.firebaseCode) //Room的update是認primary key下去改的
                    eventDao.update(dataEntity)
                }
            }
            FirebaseData.TYPE_CANCEL->{

            }
            FirebaseData.TYPE_MOVE->{

            }
        }
    }


}