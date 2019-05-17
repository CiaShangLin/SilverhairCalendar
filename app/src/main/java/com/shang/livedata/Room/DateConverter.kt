package com.shang.livedata.Room

import androidx.room.TypeConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

class DateConverter {
    @TypeConverter
    fun stringToCalendarDay(time: String): CalendarDay {
        var sp = time.split("-")
        return CalendarDay.from(sp[0].toInt(), sp[1].toInt(), sp[2].toInt())
    }

    @TypeConverter
    fun calendarDayToString(calendarDay: CalendarDay): String {
        return StringBuffer("")
            .append(calendarDay.year)
            .append("-")
            .append(calendarDay.month)
            .append("-")
            .append(calendarDay.day).toString()
    }

    companion object{
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
}
