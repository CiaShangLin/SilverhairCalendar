package com.shang.livedata.Firebase

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao

class FirebaseDao {

    private lateinit var query: DatabaseReference
    private lateinit var firebaseLiveData: FirebaseLiveData

    constructor(dataRepository: DataRepository) {
        query = FirebaseDatabase.getInstance().getReference(dataRepository.getSetting().firebaseCode)
        firebaseLiveData = FirebaseLiveData(query, dataRepository)
    }


    fun getFirebaseData(): FirebaseLiveData {
        return firebaseLiveData
    }

    //給model用的
    fun push(dataEntity: DataEntity) {
        var key: String? = query.push()?.key
        if (key != null) {
            dataEntity.firebaseCode = if (key != null) key else "null"
            query.child(key).updateChildren(dataEntity.toMap())
        }
    }

    fun update(dataEntity: DataEntity) {
        query.child(dataEntity.firebaseCode).updateChildren(dataEntity.toMap())
    }

    fun delete(dataEntity: DataEntity) {
        query.child(dataEntity.firebaseCode).removeValue()
    }

}