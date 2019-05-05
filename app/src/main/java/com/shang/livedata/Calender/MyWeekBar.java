package com.shang.livedata.Calender;

import android.content.Context;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;

public class MyWeekBar extends WeekBar {
    public MyWeekBar(Context context) {
        super(context);
    }

    @Override
    protected void setTextColor(int color) {
        super.setTextColor(color);
    }

    @Override
    protected void setTextSize(int size) {
        super.setTextSize(size);
    }

    @Override
    protected void onDateSelected(Calendar calendar, int weekStart, boolean isClick) {
        super.onDateSelected(calendar, weekStart, isClick);
    }

    @Override
    protected void onWeekStartChange(int weekStart) {
        super.onWeekStartChange(weekStart);
    }

    @Override
    protected int getViewIndexByCalendar(Calendar calendar, int weekStart) {
        return super.getViewIndexByCalendar(calendar, weekStart);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
