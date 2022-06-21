package com.example.test_project_1

import android.app.Activity
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
        addbtn = findViewById(R.id.addbtn)
        savebtn = findViewById(R.id.savebtn)

        val addrecyview = findViewById<RecyclerView>(R.id.addrecyview)

        val tDatas: java.util.ArrayList<TestModel> = arrayListOf(
            TestModel(0, "쌀밥", 335),
            TestModel(1, "김치볶음밥", 551),
            TestModel(2, "카레라이스", 518),
            TestModel(3, "참치김밥", 435),
            TestModel(4, "일반김밥", 323),
            TestModel(5, "라면", 451),
            TestModel(6, "물냉면", 384),
            TestModel(7, "비빔냉면", 499),
            TestModel(8, "짜장면", 529),
            TestModel(9, "짬뽕", 464),
            TestModel(10, "군만두(1개)", 77),
            TestModel(11, "미역국", 46),
            TestModel(12, "뼈다귀해장국", 714),
            TestModel(13, "된장찌개", 92),
            TestModel(14, "김치찌개", 243),
            TestModel(15, "닭갈비", 596),
            TestModel(16, "불고기", 450),
            TestModel(17, "떡볶이", 260),
            TestModel(18, "치킨(1조각)", 249),
            TestModel(19, "탕수육", 359),
            TestModel(20, "단무지", 4),
            TestModel(21, "김치", 11),
            TestModel(22, "멸치볶음", 138),
            TestModel(23, "깍두기", 16),
            TestModel(24, "도토리묵", 148),
            TestModel(25, "시금치", 34),
            TestModel(26, "잡채", 291),
            TestModel(27, "돈까스", 464),
            TestModel(28, "족발", 583),
            TestModel(29, "계란찜", 210),
            TestModel(30, "주먹밥", 210),
            TestModel(31, "볶음밥", 640),
            TestModel(32, "비빔밥", 638),
            TestModel(33, "새우볶음밥", 636),
            TestModel(34, "오므라이스", 693),
            TestModel(35, "불고기 덮밥", 728),
            TestModel(36, "오징어 덮밥", 485),
            TestModel(37, "제육덮밥", 950),
            TestModel(38, "회덮밥", 594),
            TestModel(39, "돼지국밥", 911),
            TestModel(40, "소고기 무국", 123),
            TestModel(41, "갈비", 240),
            TestModel(42, "두부조림", 72),
            TestModel(43, "계란말이", 193),
            TestModel(44, "어묵볶음", 275),
            TestModel(45, "소세지볶음", 471),
            TestModel(46, "콩나물", 35),
            TestModel(47, "닭칼국수", 663),
            TestModel(48, "막국수", 799),
            TestModel(49, "비빔국수", 512),
            TestModel(50, "치킨무", 22)
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
        var time = intent.getStringExtra(("time")) as String

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
            add(mDatas, name, time.toInt(), weight, kcal)
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
                            var outIntent = Intent(applicationContext, CalendarPage::class.java)
                            setResult(Activity.RESULT_OK, outIntent)
                            finish()
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
                kcal = (it.kcal * weight).toInt()
            }
        }
        return kcal
    }
}