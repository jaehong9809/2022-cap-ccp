package com.example.test_project_1.calrecy

import java.text.SimpleDateFormat
import java.util.*

data class CalendarDateModel(var data: Date, var isSelected: Boolean = false) {

    val cal_week: String
        get() = SimpleDateFormat("EE", Locale.getDefault()).format(data)

    val cal_day: String
        get() {
            val cal = Calendar.getInstance()
            cal.time = data
            return cal[Calendar.DAY_OF_MONTH].toString()
        }
}