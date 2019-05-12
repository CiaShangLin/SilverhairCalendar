package com.shang.livedata.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.shang.livedata.Firebase.FirebaseLiveData
import com.shang.livedata.Repository.FirebaseRepository
import com.shang.livedata.Room.DataEntity

class FirebaseViewModel: AndroidViewModel{

    private lateinit var firebaseRepository:FirebaseRepository

    constructor(application: Application):super(application){
        firebaseRepository= FirebaseRepository(application)
    }

    //FirebaseDao
    fun getFirebaseLiveData(): FirebaseLiveData {
        return firebaseRepository.getFirebaseLiveData()
    }

    fun pushFirebase(dataEntity: DataEntity){
        firebaseRepository.pushFirebase(dataEntity)
    }

    fun updateFirebase(dataEntity: DataEntity){
        firebaseRepository.updateFirebase(dataEntity)
    }
}