package com.shang.livedata.Repository

import android.content.Context
import androidx.room.Room
import com.shang.livedata.Firebase.FirebaseDao
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao
import com.shang.livedata.Room.RoomDatabase

class FirebaseRepository{
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


}