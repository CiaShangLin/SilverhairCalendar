package com.shang.livedata.Main

import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.content.Context


class MyDayView(var context: Context, var hashMap: HashSet<CalendarDay>, var color:Int, var drawable: Drawable) :DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return hashMap.contains(day)

    }

    override fun decorate(view: DayViewFacade?) {
        //view?.addSpan(DotSpan(5f,color))
        view?.setBackgroundDrawable(drawable)
        //view?.addSpan(ForegroundColorSpan(ContextCompat.getColor(context,android.R.color.black)));
    }
}