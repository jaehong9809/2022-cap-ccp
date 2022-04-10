package com.example.test_project_1

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.addrecy.AddAdapter
import com.example.test_project_1.addrecy.AddModel
import com.example.test_project_1.foodrecy.FoodInfoAdapter
import com.example.test_project_1.foodrecy.FoodModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddPage : AppCompatActivity() {
    private lateinit var addname: Spinner
    private lateinit var addweight: Spinner
    private lateinit var addunit: Spinner
    private lateinit var addtime: Spinner

    private lateinit var addbtn: Button
    private lateinit var savebtn: Button

    private var adddate: Int = 0

    private val failtext = "값을 입력하세요."
    private val succtext = "추가되었습니다!"

    private var mDatas: ArrayList<AddModel> = arrayListOf(
        AddModel("", 0,  "", 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_page)

        addname = findViewById(R.id.addname)
        addweight = findViewById(R.id.addweight)
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


        var id = intent.getStringExtra("textId") as String
        var sex = intent.getStringExtra("sex") as String
        var user_weight = intent.getIntExtra("weight", 0)
        var height = intent.getIntExtra("height", 0)
        var age = intent.getIntExtra("age", 0)

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
                adddate = cyear*100000 + (cmonth+1)*1000 + cday*10
                adddate += position
            }
        }

        var weight = ""
        ArrayAdapter.createFromResource(this, R.array.dietweight, android.R.layout.simple_spinner_item)
            .also { spiadapter -> spiadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                addweight.adapter = spiadapter
            }
        addweight.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected (parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                weight = addweight.getSelectedItem().toString()
            }
        }

        val addadapter= AddAdapter(this, mDatas)
        addrecyview.adapter=addadapter
        val mLayoutManager = LinearLayoutManager(this)
        addrecyview.layoutManager = mLayoutManager
        addrecyview.setHasFixedSize(true)

        addbtn.setOnClickListener {
            if(mDatas.get(0).add_name == "") {
                mDatas.clear()
            }
            val kcal = getkcal(tDatas, name, weight.toFloat())
            add(mDatas, name, adddate, weight, kcal)
            gettoast(succtext)
            addadapter.setData()
        }

        savebtn.setOnClickListener {
            var retro = Retro()
            var retrofit = retro.retrofit
            var savefood = retrofit.create(SaveFood::class.java)

            mDatas.forEach{
                savefood.saveFood(it.add_name, it.add_date, it.add_weight, id, sex, user_weight, height, age).enqueue(object: Callback<Food> {
                    override fun onResponse(call: Call<Food>, response: Response<Food>) {
                        var food = response.body() as Food
                        if(food.code == "0000"){
                            Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(applicationContext, "없어", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Food>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            finish()
        }
    }

    fun gettoast(text: String) {
        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun add(mDatas: ArrayList<AddModel>, addname: String, adddate: Int, addweight: String, addkcal: Int) {
        with(mDatas){
            add(AddModel(addname, adddate, addweight, addkcal))
        }
    }

    fun getkcal(tDatas: ArrayList<TestModel>, name: String, weight: Float): Int {
        var kcal = 0
        tDatas.forEach {
            if(it.name == name) {
                var thiskcal = (it.carb * 4) + (it.prot * 4) + (it.fat) * 9
                kcal = (thiskcal * weight).toInt()
            }
        }
        return kcal
    }
}