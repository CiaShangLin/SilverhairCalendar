package com.shang.livedata.TimeClock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import com.shang.livedata.Dialog.AddDialog
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.Room.DateConverter
import java.util.*

class MyAlarmManager(private var context: Context?) {

    private val alarmManager: AlarmManager by lazy {
        context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    //設定提醒
    fun setTimeClock(dataEntity: DataEntity) {
        var intent = Intent(context, MyBroadcastReceiver::class.java).apply {
            this.action = MyBroadcastReceiver.ACTION
            this.putExtra(MyBroadcastReceiver.TITLE, dataEntity.name)
            this.putExtra(MyBroadcastReceiver.CONTENT, dataEntity.event)
        }
        var pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, getCalendarTime(dataEntity), pendingIntent)
    }

    fun cancelTimeClock(){
        //alarmManager.cancel(null)
    }


    private fun getCalendarTime(dataEntity: DataEntity): Long {
        var calendarDay = DateConverter.stringToCalendarDay(dataEntity.calendarDayString)
        var calendar = Calendar.getInstance()
        with(calendar) {
            this.set(Calendar.YEAR, calendarDay.year)
            this.set(Calendar.MONTH, calendarDay.month - 1)  //Calendar的月份是從0~11
            this.set(Calendar.DAY_OF_MONTH, calendarDay.day)
            this.set(Calendar.HOUR_OF_DAY, dataEntity.hour)
            this.set(Calendar.MINUTE, dataEntity.minute)
        }
        Log.v(AddDialog.TAG, "${calendar.time}")
        return calendar.time.time
    }

}