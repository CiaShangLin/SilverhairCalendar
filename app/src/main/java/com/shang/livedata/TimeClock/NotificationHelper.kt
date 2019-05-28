package com.shang.livedata

import android.content.Context
import android.content.ContextWrapper

import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.annotation.TargetApi
import androidx.core.app.NotificationCompat


class NotificationHelper : ContextWrapper {
    companion object {
        val channelID = "channelID"
        val channelName = "Channel Name"
    }

    private var mManager: NotificationManager? = null
    constructor(context: Context):super(context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (mManager == null) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager!!
    }

    fun getChannelNotification(title:String,content:String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_calendar)
    }
}