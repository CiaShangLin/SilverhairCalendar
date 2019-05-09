package com.shang.livedata.Firebase

import com.google.firebase.database.FirebaseDatabase
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao

class FirebaseDao(eventDao: EventDao) {

    var query = FirebaseDatabase.getInstance().getReference("TO0GbWYZ51d4RW95HZd3boY0mv62")
    var firebaseLiveData = FirebaseLiveData(query, eventDao)

    fun getFirebaseData(): FirebaseLiveData {
        return firebaseLiveData
    }

    //給model用的
    fun push(dataEntity: DataEntity) {
        var key: String? = query.push()?.key
        if(key!=null){
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