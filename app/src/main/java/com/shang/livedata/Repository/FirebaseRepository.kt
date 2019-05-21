package com.shang.livedata.Repository

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.shang.livedata.Firebase.FirebaseDao
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao
import com.shang.livedata.Room.RoomDatabase
import com.shang.livedata.ViewModel.DataViewModel

class FirebaseRepository {
    private lateinit var firebaseDao: FirebaseDao
    private lateinit var dataRepository: DataRepository

    constructor(context: Context) {
        dataRepository = DataRepository(context)
        firebaseDao = FirebaseDao(dataRepository)
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