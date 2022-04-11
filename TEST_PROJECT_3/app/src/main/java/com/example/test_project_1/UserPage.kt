package com.example.test_project_1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class UserPage : Fragment() {
    data class dayCalorie(var day:String, var calorie:Int)  //날짜, 칼로리 클래스
    data class userData(var name:String, var age:Int, var weight:Int, var sex:String, var daesa:Int, var height:Int)
    lateinit var chart1:LineChart
    lateinit var chart2:LineChart
    lateinit var agetv:TextView
    lateinit var weight:TextView
    lateinit var sextv:TextView
    lateinit var bmitv:TextView
    lateinit var daesatv:TextView
    lateinit var heighttv:TextView
    lateinit var username:TextView
    lateinit var modifybt:Button
    lateinit var advtv:TextView
    lateinit var advimv:ImageView
    var standardcal=0
    var day30totalcal=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_page, container, false)

        var id = setid
        var user_sex = setsex
        var user_weight = setweight
        var user_height = setheight
        var user_age = setage

        chart1=view.findViewById(R.id.chart1)            //15일
        chart2=view.findViewById(R.id.chart2)            //30일

        agetv=view.findViewById(R.id.agetv)              //testview들
        weight=view.findViewById(R.id.weighttv)
        sextv=view.findViewById(R.id.sextv)
        bmitv=view.findViewById(R.id.bmitv)
        daesatv=view.findViewById(R.id.daesatv)
        heighttv=view.findViewById(R.id.heighttv)
        username=view.findViewById(R.id.username)
        advtv=view.findViewById(R.id.advtv1)
        advimv=view.findViewById(R.id.advimv1)
        modifybt=view.findViewById(R.id.modifybt)
        var userone=userData(id,user_age, user_weight, user_sex, 1700, user_height)
        var qq=((userone.weight)/((userone.height*0.01)*(userone.height*0.01))).toInt()
        agetv.text=Integer.toString(userone.age)
        weight.text=Integer.toString(userone.weight)
        sextv.text=userone.sex
        daesatv.text=Integer.toString(userone.daesa)
        heighttv.text=Integer.toString(userone.height)
        username.text=userone.name
        bmitv.text=qq.toString()


        modifybt.setOnClickListener {
            var intent = Intent(getActivity(), ModifyPage::class.java)
            startActivityForResult(intent, 0)
        }

        var retro = Retro()
        var retrofit = retro.retrofit
        var userInfo = retrofit.create(UserInfo::class.java)

        userInfo.search(id).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                var result = response.body()
                if(result?.code =="0000"){
                    var day15 = result.day15
                    var day30 = result.day30
                    var entries15 =ArrayList<dayCalorie>()    //30일짜리 데이터 셋
                    var index=0
                    for (i in day15){
                        index++
                        var str=i[0].substring(4,6)+"/"+i[0].substring(6, 8)
                        var calorie=i[1].toInt()
                        var tmp:dayCalorie
                        if(index%2==0){
                            tmp= dayCalorie(str, calorie)
                        }
                        else{
                            tmp= dayCalorie(" ", calorie)
                        }
                        entries15.add(tmp)
                    }
                    makechart(chart1, entries15, 15)                       //그래프 생성
                    var entries30 =ArrayList<dayCalorie>()    //30일짜리 데이터 셋
                    index=0
                    for (i in day30){
                        index++
                        var str=i[0].substring(4,6)+"/"+i[0].substring(6, 8)
                        var calorie=i[1].toInt()
                        day30totalcal+=calorie
                        var tmp:dayCalorie
                        if(index%3==0){
                            tmp= dayCalorie(str, calorie)
                        }
                        else{
                            tmp=dayCalorie(" ", calorie)
                        }
                        entries30.add(tmp)
                    }
                    makechart(chart2, entries30, 30)                       //그래프 생성
                    if(user_sex.equals("M")){
                        standardcal=when(user_age){
                            in 1..2 -> 900
                            in 3..5 -> 1400
                            in 6..8 -> 1700
                            in 9..11 -> 2000
                            in 12..14 -> 2500
                            in 15..18 ->2700
                            in 19..29 ->2600
                            in 30..49 -> 2500
                            in 50..64 ->2200
                            in 65..74-> 2000
                            in 75..150->1900
                            else ->1900
                        }
                    }
                    else{
                        standardcal=when(user_age){
                            in 1..2 -> 900
                            in 3..5 -> 1400
                            in 6..8 -> 1500
                            in 9..11 -> 1800
                            in 12..14 -> 2000
                            in 15..18 ->2000
                            in 19..29 ->2000
                            in 30..49 -> 1900
                            in 50..64 ->1700
                            in 65..74-> 1600
                            in 75..150->1500
                            else ->1900
                        }
                    }
                    standardcal=(standardcal*30)
                    if(day30totalcal>standardcal+1000){                 //기준+1000 보다 큼 -> 잘못됨
                        advtv.text="잘못된 식습관입니다! 칼로리를 조절하시오!"
                        advimv.setImageResource(R.drawable.star_bad)
                    }
                    else if((day30totalcal>standardcal-1000)&&(day30totalcal<standardcal+1000)){    //기준-1000 < <기준 +1000 사이 -> good
                        advtv.text="좋은 식습관입니다! 지금을 유지하십시오!"
                        advimv.setImageResource(R.drawable.star_good)
                    }
                    else if(day30totalcal<standardcal-1000){                //기준 -1000보다 작음 -> 아주 굳
                        advtv.text="당신은 완벽한 다이어트를 하고있습니다! 완벽한 몸매에 도전하시오!"
                        advimv.setImageResource(R.drawable.star_verygood)
                    }
                }
                else{
                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
            }

        })


        /*  modifybt.setOnClickListener {
              Toast.makeText(requireContext(), "수정어케하지", Toast.LENGTH_LONG).show()
          }*/


        var cal= Calendar.getInstance()      //오늘날짜
        var cmonth=cal.get(Calendar.MONTH)
        var cday=cal.get(Calendar.DAY_OF_MONTH)

        var entries =ArrayList<dayCalorie>()    //30일짜리 데이터 셋
        for (i in 1..30){

            var str=Integer.toString(cmonth+1)+"/"+Integer.toString(cday)
            var calorie=2000+(i*100)
            var tmp:dayCalorie= dayCalorie(str, calorie)
            entries.add(tmp)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            var qq=((setweight)/((setheight*0.01)*(setheight*0.01))).toInt()
            agetv.text=Integer.toString(setage)
            weight.text=Integer.toString(setweight)
            sextv.text=setsex
            heighttv.text=Integer.toString(setheight)
            username.text=setid
            bmitv.text=qq.toString()
            }
    }

    fun makechart(chart1: LineChart, entries: ArrayList<dayCalorie>, daynum:Int){
        val entryList= mutableListOf<Entry>()
        for(i in 1..daynum){
            entryList.add(Entry(i.toFloat(), entries[i-1].calorie.toFloat()))
        }
        val bardataset= LineDataSet(entryList, "Mybardataset")
        bardataset.setDrawCircleHole(false)
        bardataset.setColor(Color.CYAN, 150)
        val bardata= LineData(bardataset)


        chart1.run(){
            legend.isEnabled=false
        }
        val xaxis=chart1.xAxis
        xaxis.setDrawLabels(true)
        xaxis.axisMaximum=daynum.toFloat()
        xaxis.axisMinimum=1f
        xaxis.labelCount=daynum-1



        xaxis.valueFormatter=object:ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return entries[value.toInt()-1].day
            }
        }

        xaxis.textColor=Color.BLACK
        xaxis.position=XAxis.XAxisPosition.BOTTOM
        xaxis.setDrawLabels(true)
        xaxis.setDrawAxisLine(true)


        var max:Int=0
        for( i in entries){
            if(i.calorie>max)
                max=i.calorie
        }

        val ylaxis=chart1.axisLeft
        ylaxis.axisMaximum=max.toFloat()
        ylaxis.axisMinimum=0f
        ylaxis.setDrawAxisLine(false)
        ylaxis.setDrawGridLines(false)

        val yraxis=chart1.axisRight
        yraxis.setDrawLabels(false)
        yraxis.setDrawAxisLine(false)
        yraxis.setDrawGridLines(false)
        chart1.setScaleEnabled(false)
        chart1.setPinchZoom(false)
        chart1.description=null
        xaxis.setDrawGridLines(false)

        chart1.animateXY(0, 800)
        chart1.data=bardata
        chart1.invalidate()
    }
}