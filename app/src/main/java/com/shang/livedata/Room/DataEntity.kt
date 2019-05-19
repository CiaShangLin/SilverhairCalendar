package com.shang.livedata.Room

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.IntegerRes
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.database.DataSnapshot
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.shang.livedata.Room.DataEntity.CREATOR.TABLE_NAME
import java.util.*

@SuppressLint("ParcelCreator")
@Entity(tableName = TABLE_NAME)

class DataEntity() :Parcelable{
    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(id)
        p0?.writeString(event)
        p0?.writeParcelable(calendarDay, p1)
        p0?.writeInt(hour)
        p0?.writeInt(minute)
        p0?.writeString(calendarDayString)
        p0?.writeInt(type)
        p0?.writeByte(if (status) 1 else 0)
        p0?.writeString(name)
        p0?.writeString(firebaseCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataEntity> {
        const val TABLE_NAME: String = "MY_DATA_TABLE"

        override fun createFromParcel(parcel: Parcel): DataEntity {
            return DataEntity(parcel)
        }

        override fun newArray(size: Int): Array<DataEntity?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        event = parcel.readString()
        calendarDay = parcel.readParcelable(CalendarDay::class.java.classLoader)
        hour = parcel.readInt()
        minute = parcel.readInt()
        calendarDayString = parcel.readString()
        type = parcel.readInt()
        status = parcel.readByte() != 0.toByte()
        name = parcel.readString()
        firebaseCode = parcel.readString()
    }



    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var event: String = ""

    @Nullable
    lateinit var calendarDay: CalendarDay  //輸入日歷用的
    var hour: Int = 0
    var minute: Int = 0

    var calendarDayString: String = ""

    var type: Int = 0   //紀錄顏色

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
        map.put("type", type)
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