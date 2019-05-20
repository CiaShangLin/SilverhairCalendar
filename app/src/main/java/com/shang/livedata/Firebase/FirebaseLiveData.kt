package com.shang.livedata.Firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.EventDao

class FirebaseLiveData(private var query: DatabaseReference, private var dataRepository: DataRepository) : LiveData<String>() {

    private val TAG: String = "FirebaseLiveData"

    private var childEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            value = "事件取消"
            Log.v(TAG, "事件取消:${p0.message}")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            value = "事件移動"
            Log.v(TAG, "事件移動:${p0.key}")
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            updateToRoom(p0)
            Log.v(TAG, "事件更新:${p0.key}")
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            //從Firebase寫入到Room不要有ID 因為ID是primaryKey不能重複 而寫入方和被寫入方會發生衝突
            insertToRoom(p0)
            Log.v(TAG, "事件新增:${p0.key}")
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            deleteToRoom(p0)
            Log.v(TAG, "事件刪除:${p0.key}")
        }

    }


    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(childEventListener)
        Log.d(TAG, "$TAG:onInactive")
    }

    override fun onActive() {
        super.onActive()
        query.addChildEventListener(childEventListener)
        Log.d(TAG, "$TAG:onActive")
    }


    //toRoom給child用
    private fun insertToRoom(dataSnapshot: DataSnapshot) {
        var dataEntity = dataSnapshotToDataEntity(dataSnapshot)
        if (dataEntity != null) {
            //檢查firebaseCode是因為每次onActive他都會跑一次
            if (!dataRepository.checkFirebaseCode(dataEntity.firebaseCode)) {
                dataRepository.insert(dataEntity)
                value = "事件新增"
            }
        }
    }

    private fun updateToRoom(dataSnapshot: DataSnapshot) {
        var dataEntity = dataSnapshotToDataEntity(dataSnapshot)
        if (dataEntity != null) {
            dataEntity.id = dataRepository.getFirebaseCodeToId(dataEntity.firebaseCode) //Room的update是認primary key下去改的
            dataRepository.update(dataEntity)
            value = "事件更新"
        }
        Log.v(TAG, "事件更新:${dataSnapshot.key}")
    }

    private fun deleteToRoom(dataSnapshot: DataSnapshot) {
        var dataEntity = dataSnapshotToDataEntity(dataSnapshot)
        if (dataEntity != null) {
            dataEntity.id = dataRepository.getFirebaseCodeToId(dataEntity.firebaseCode)
            dataRepository.delete(dataEntity)
            value = "事件移除"
            Log.v(TAG, "事件移除:${dataSnapshot.key}")
        }
    }

    //Firebase轉換成DataEntity
    private fun dataSnapshotToDataEntity(dataSnapshot: DataSnapshot): DataEntity? {
        var dataEntity: DataEntity? = dataSnapshot.getValue(DataEntity::class.java)
        if (dataEntity != null) {
            dataEntity.calendarDay = dataEntity.stringToCalendarDay(dataEntity.calendarDayString) //轉換CalendarDay
        }
        return dataEntity
    }

}