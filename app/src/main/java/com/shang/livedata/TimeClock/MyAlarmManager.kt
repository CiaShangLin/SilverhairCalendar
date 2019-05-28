package com.shang.livedata

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.shang.livedata.Room.DataEntity
import java.util.*

class MyAlarmManager(var context: Context) {

     val alarmManager:AlarmManager by lazy {
         context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
     }

    fun setTimeClock(dataEntity:DataEntity){
        var intent = Intent(context, MyBroadcastReceiver::class.java).apply {
            this.action = MyBroadcastReceiver.ACTION
            this.putExtra(MyBroadcastReceiver.TITLE, dataEntity.name)
            this.putExtra(MyBroadcastReceiver.CONTENT, dataEntity.event)
        }
        var pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC, Date().time, pendingIntent)
    }



}