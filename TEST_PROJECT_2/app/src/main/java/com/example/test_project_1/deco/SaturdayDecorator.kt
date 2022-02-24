package com.example.demo02.deco

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.*
import java.util.*

class SaturdayDecorator: DayViewDecorator {
    private val calendar = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
    }
}