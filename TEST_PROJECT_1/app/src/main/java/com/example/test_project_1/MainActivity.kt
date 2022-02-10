package com.example.test_project_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import org.w3c.dom.Text
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var cv1:CalendarView
    lateinit var tv1:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cv1=findViewById(R.id.cv1)
        tv1=findViewById(R.id.tv1)
        var cal= Calendar.getInstance()
        var cyear=cal.get(Calendar.YEAR)
        var cmonth=cal.get(Calendar.MONTH)
        var cday=cal.get(Calendar.DAY_OF_MONTH)
        tv1.text=Integer.toString(cyear)+"_"+Integer.toString(cmonth+1)+"_"+Integer.toString(cday)
        cv1.setOnDateChangeListener { calendarView, i, i2, i3 ->
            tv1.text=Integer.toString(i)+"_"+Integer.toString(i2+1)+"_"+Integer.toString(i3)
        }
    }
}