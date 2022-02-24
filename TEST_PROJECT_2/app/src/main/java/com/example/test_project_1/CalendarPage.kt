package com.example.test_project_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.w3c.dom.Text
import java.util.*

class CalendarPage : Fragment() {
    lateinit var cv1: MaterialCalendarView
    lateinit var tv1: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar_page, container, false)

        cv1=view.findViewById(R.id.calendar)
        tv1=view.findViewById(R.id.todaydate)

        var cal= Calendar.getInstance()
        var cyear=cal.get(Calendar.YEAR)
        var cmonth=cal.get(Calendar.MONTH)
        var cday=cal.get(Calendar.DAY_OF_MONTH)
        tv1.text=Integer.toString(cyear)+"_"+Integer.toString(cmonth+1)+"_"+Integer.toString(cday)

        return view
    }
}