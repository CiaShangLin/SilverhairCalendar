package com.shang.livedata.Room

import androidx.annotation.IntegerRes
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.database.DataSnapshot
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Room.DataEntity.Companion.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
class DataEntity {

    companion object {
        const val TABLE_NAME: String = "MY_DATA_TABLE"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var event: String = ""

    @Nullable
    lateinit var calendarDay: CalendarDay  //輸入日歷用的
    var hour: Int = 0
    var minute: Int = 0

    var calendarDayString: String = ""

    var color: Int = 0   //紀錄顏色

    var status: Boolean = false  //事件狀態

    @Nullable
    var name: String = ""   //寫入人的名稱

    var firebaseCode: String = "null"


    //calendarDay不能直接寫入到Firebase 他不認識這東西
    fun toMap(): MutableMap<String, Any> {
        var map = mutableMapOf<String, Any>()
        map.put("event", event)
        map.put("hour", hour)
        map.put("minute", minute)
        map.put("color", color)
        map.put("status", status)
        map.put("name", name)
        map.put("firebaseCode", firebaseCode)
        map.put("calendarDayString", calendarDayString)
        return map
    }

    fun stringToCalendarDay(time: String): CalendarDay {
        var sp = time.split("-")
        return CalendarDay.from(sp[0].toInt(), sp[1].toInt(), sp[2].toInt())
    }

    fun calendarDayToString(calendarDay: CalendarDay): String {
        return StringBuffer("")
            .append(calendarDay.year)
            .append("-")
            .append(calendarDay.month)
            .append("-")
            .append(calendarDay.day).toString()
    }


}