package com.shang.livedata.Main

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.content.Context
import android.icu.text.UnicodeSet
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.shang.livedata.R


class MyDayView : DayViewDecorator {
    private var mHashSet = HashSet<CalendarDay>()
    private var color: Int = R.color.blue

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, hashSet: HashSet<CalendarDay>) {
        this.color = context.resources.getColor(R.color.blue)
        this.mHashSet = hashSet
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return mHashSet.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5f, color))

        //view?.setBackgroundDrawable(drawable)

        //view?.addSpan(ForegroundColorSpan(ContextCompat.getColor(context,R.color.blue)))

    }
}