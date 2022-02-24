package com.example.test_project_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.w3c.dom.Text
import java.util.*

class MainActivity : AppCompatActivity() {
    private val calfrag by lazy { CalendarPage() }
    private val infofrag by lazy { InfoPage() }
    private val userfrag by lazy { UserPage() }

    lateinit var touserbt:Button
    lateinit var toinfobt:Button
    lateinit var tocalbt:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeFragment(calfrag)
        FragmentPage()
    }

    private fun FragmentPage() {
        touserbt = findViewById(R.id.touserpagebt)
        toinfobt = findViewById(R.id.toinfopagebt)
        tocalbt = findViewById(R.id.tocalpagebt)

        tocalbt.setOnClickListener {
            changeFragment(calfrag)
        }
        touserbt.setOnClickListener {
            changeFragment(userfrag)
        }
        toinfobt.setOnClickListener {
            changeFragment(infofrag)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_, fragment)
            .commit()
    }
}