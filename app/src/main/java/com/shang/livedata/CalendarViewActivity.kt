package com.shang.livedata

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_calendar_view.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shang.livedata.Room.DataEntity
import com.shang.livedata.ViewModel.DataViewModel
import org.jetbrains.anko.toast
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarViewActivity : AppCompatActivity() {

    lateinit var model: DataViewModel
    lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)


        cal
            .state()
            .edit()
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()


        cal.setOnDateChangedListener { widget, calendarDay, selected ->
            var year=calendarDay.year
            var month=calendarDay.month
            var day=calendarDay.day
            toast("$year-$month-$day")

        }


        var set = hashSetOf<CalendarDay>().apply {
            this.add(CalendarDay.from(2019, 4, 19))
            this.add(CalendarDay.from(2019, 4, 10))
            this.add(CalendarDay.from(2019, 4, 5))
        }
        cal.addDecorator(
            MyDayView(
                this,
                set,
                resources.getColor(R.color.primary_dark_material_dark),
                resources.getDrawable(R.drawable.ic_ellipsis)
            )
        )

        collapsing_toolbar_layout.isTitleEnabled=false



        toolbar.inflateMenu(R.menu.menu_calendar)


    }
}
