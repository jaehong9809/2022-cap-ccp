package com.example.test_project_1

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.addrecy.AddAdapter
import com.example.test_project_1.addrecy.AddModel
import java.util.*
import kotlin.collections.ArrayList

class AddPage : AppCompatActivity() {
    private lateinit var addname: Spinner
    private lateinit var addweight: TextView
    private lateinit var addunit: Spinner
    private lateinit var addtime: Spinner

    private lateinit var addbtn: Button
    private lateinit var savebtn: Button

    private var adddate: Int = 0

    private val failtext = "값을 입력하세요."
    private val succtext = "추가되었습니다!"

    private var mDatas: ArrayList<AddModel> = arrayListOf(
        AddModel("", 0, 0, "", 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_page)

        addname = findViewById(R.id.addname)
        addweight = findViewById(R.id.addweight)
        addunit = findViewById(R.id.addunit)
        addtime = findViewById(R.id.addtime)
        addbtn = findViewById(R.id.addbtn)
        savebtn = findViewById(R.id.savebtn)

        val addrecyview = findViewById<RecyclerView>(R.id.addrecyview)

        val tDatas: java.util.ArrayList<TestModel> = arrayListOf(
            TestModel("냉면", 700, 72, 14, 4),
            TestModel("돈까스", 200, 23, 34, 26),
            TestModel("단팥죽", 100, 24, 4, 1),
            TestModel("짬뽕밥", 900, 105, 29, 13)
        )

        var cal= Calendar.getInstance()
        var cyear=cal.get(Calendar.YEAR)
        var cmonth=cal.get(Calendar.MONTH)
        var cday=cal.get(Calendar.DAY_OF_MONTH)

        var name = ""
        ArrayAdapter.createFromResource(this, R.array.dietname, android.R.layout.simple_spinner_item)
            .also { spiadapter -> spiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                addname.adapter = spiadapter
            }
        addname.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected (parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                name = addname.getSelectedItem().toString()
            }
        }

        ArrayAdapter.createFromResource(this, R.array.diettime, android.R.layout.simple_spinner_item)
            .also { spiadapter -> spiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                addtime.adapter = spiadapter
            }
        addtime.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected (parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                adddate = cyear*100000 + cmonth*1000 + cday*10
                adddate += position
            }
        }

        var unit = ""
        ArrayAdapter.createFromResource(this, R.array.dietunit, android.R.layout.simple_spinner_item)
            .also { spiadapter -> spiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                addunit.adapter = spiadapter
            }
        addunit.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected (parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                unit = addunit.getSelectedItem().toString()
            }
        }

        val addadapter= AddAdapter(this, mDatas)
        addrecyview.adapter=addadapter
        val mLayoutManager = LinearLayoutManager(this)
        addrecyview.layoutManager = mLayoutManager
        addrecyview.setHasFixedSize(true)

        addbtn.setOnClickListener {
            var weight = addweight.text.toString()
            if(weight == "") {
                gettoast(failtext)
            }
            else {
                var weightInt = Integer.parseInt(weight)
                if(mDatas.get(0).add_name == "") {
                    mDatas.clear()
                }
                val kcal = getkcal(tDatas, name, weightInt, unit)
                add(mDatas, name, adddate, weightInt, unit, kcal)
                gettoast(succtext)
                addadapter.setData()
            }
        }
    }

    fun gettoast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun add(mDatas: ArrayList<AddModel>, addname: String, adddate: Int, addweight: Int, addunit: String, addkcal: Int) {
        with(mDatas){
            add(AddModel(addname, adddate, addweight, addunit, addkcal))
        }
    }

    fun getkcal(tDatas: ArrayList<TestModel>, name: String, weight: Int, unit: String): Int {
        var kcal = 0
        tDatas.forEach {
            if(it.name == name) {
                var thiskcal = (it.carb * 4) + (it.prot * 4) + (it.fat) * 9
                if (unit == "g") {
                    kcal = (thiskcal * ((weight * 100) / it.weight)) / 100
                }
                else {
                    kcal = thiskcal * weight
                }
            }
        }
        return kcal
    }
}