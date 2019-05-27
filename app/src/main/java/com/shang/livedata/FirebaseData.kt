package com.shang.livedata

import com.google.firebase.database.DataSnapshot

data class FirebaseData(var data: DataSnapshot, var type: Int) {
    companion object {
        val TYPE_INSERT = 1
        val TYPE_DELETE = 2
        val TYPE_UPDATE = 3
        val TYPE_CANCEL = 4
        val TYPE_MOVE = 5
    }
}