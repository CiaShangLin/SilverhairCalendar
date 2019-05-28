package com.shang.livedata.Firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.shang.livedata.Repository.DataRepository
import com.shang.livedata.Room.DataEntity

class FirebaseLiveData(private var query: DatabaseReference, private var dataRepository: DataRepository) : LiveData<FirebaseData>() {

    private val TAG: String = "FirebaseLiveData"

    private var childEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            //value = "事件取消"

            Log.v(TAG, "事件取消:${p0.message}")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            value = FirebaseData(p0,FirebaseData.TYPE_MOVE)
            Log.v(TAG, "事件移動:${p0.key}")
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            value = FirebaseData(p0,FirebaseData.TYPE_UPDATE)
            Log.v(TAG, "事件更新:${p0.key}")
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            //從Firebase寫入到Room不要有ID 因為ID是primaryKey不能重複 而寫入方和被寫入方會發生衝突
            value = FirebaseData(p0,FirebaseData.TYPE_INSERT)
            Log.v(TAG, "事件新增:${p0.key}")
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            value = FirebaseData(p0,FirebaseData.TYPE_DELETE)
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

}