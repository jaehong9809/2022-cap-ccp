package com.example.test_project_1

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test_project_1.foodrecy.FoodModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val calfrag by lazy { CalendarPage() }
    private val infofrag by lazy { InfoPage() }
    private val userfrag by lazy { UserPage() }

    lateinit var touserbt:Button
    lateinit var toinfobt:Button
    lateinit var tocalbt:Button

    var mData: ArrayList<FoodModel> = arrayListOf(
        FoodModel( "떡볶이", 202202161, 200, 5, 4, 10),
        FoodModel( "피자", 202202252, 100, 15, 2, 3),
        FoodModel( "카레", 202202053, 500, 12, 15, 30)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var inten = intent
        var textId = inten.getStringExtra("textId")
        var sex = inten.getStringExtra("sex")
        var weight = inten.getIntExtra("weight", 0)
        var height = inten.getIntExtra("height", 0)
        var age = inten.getIntExtra("age", 0)

        changeFragment(calfrag)
        FragmentPage()

        intent.putExtra("DataList", mData)
        intent.putExtra("textId", textId)
        intent.putExtra("sex", sex)
        intent.putExtra("weight", weight)
        intent.putExtra("height", height)
        intent.putExtra("age", age)
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