package com.example.demo02.deco

import android.graphics.Typeface
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.*

class BoldDecorator(min:CalendarDay, max:CalendarDay):DayViewDecorator {
    val maxDay = max
    val minDay = min
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return (day?.month == maxDay.month && day.day <= maxDay.day)
                || (day?.month == minDay.month && day.day >= minDay.day)
                || (minDay.month < day?.month!! && day.month < maxDay.month)
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object: StyleSpan(Typeface.BOLD){})
        view?.addSpan(object: RelativeSizeSpan(1.4f){})
    }
}