package com.shang.livedata.Main

import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.content.Context
import com.shang.livedata.R


class MyDayView : DayViewDecorator {
    private var mHashSet = HashSet<CalendarDay>()
    private lateinit var drawable: Drawable
    private var color: Int = R.color.blue

    constructor(hashSet: HashSet<CalendarDay>, color: Int, drawable: Drawable) {
        this.drawable = drawable
        this.color = color
        this.mHashSet=hashSet
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return mHashSet.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        //view?.addSpan(DotSpan(5f,color))
        view?.setBackgroundDrawable(drawable)

        //view?.addSpan(ForegroundColorSpan(ContextCompat.getColor(context,android.R.color.black)));
    }
}