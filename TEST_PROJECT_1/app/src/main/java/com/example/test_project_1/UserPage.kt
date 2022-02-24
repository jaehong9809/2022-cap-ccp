package com.example.test_project_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList

class UserPage : AppCompatActivity() {
    data class dayCalorie(var day:String, var calorie:Int)  //날짜, 칼로리 클래스
    data class userData(var name:String, var age:Int, var weight:Int, var sex:String, var daesa:Int, var height:Int)
    lateinit var chart1:BarChart
    lateinit var chart2:BarChart
    lateinit var agetv:TextView
    lateinit var weight:TextView
    lateinit var sextv:TextView
    lateinit var bmitv:TextView
    lateinit var daesatv:TextView
    lateinit var heighttv:TextView
    lateinit var username:TextView
    lateinit var modifybt:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        chart1=findViewById(R.id.chart1)            //15일
        chart2=findViewById(R.id.chart2)            //30일

        agetv=findViewById(R.id.agetv)              //testview들
        weight=findViewById(R.id.weighttv)
        sextv=findViewById(R.id.sextv)
        bmitv=findViewById(R.id.bmitv)
        daesatv=findViewById(R.id.daesatv)
        heighttv=findViewById(R.id.heighttv)
        username=findViewById(R.id.username)
        modifybt=findViewById(R.id.modifybt)
        var userone=userData("홍길동",25, 69, "남", 1700, 173)
        var qq=((userone.weight)/((userone.height*0.01)*(userone.height*0.01))).toInt()
        agetv.text=Integer.toString(userone.age)
        weight.text=Integer.toString(userone.weight)
        sextv.text=userone.sex
        daesatv.text=Integer.toString(userone.daesa)
        heighttv.text=Integer.toString(userone.height)
        username.text=userone.name
        bmitv.text=qq.toString()


        modifybt.setOnClickListener {
            Toast.makeText(applicationContext, "수정어케하지", Toast.LENGTH_LONG).show()
        }


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


        makechart(chart1, entries, 15)                       //그래프 생성
        makechart(chart2, entries, 30)
    }
    fun makechart(chart1: BarChart, entries: ArrayList<dayCalorie>, daynum:Int){
        val entryList= mutableListOf<BarEntry>()
        for(i in 1..daynum){
            entryList.add(BarEntry(i.toFloat(), entries[i-1].calorie.toFloat()))
        }
        val bardataset=BarDataSet(entryList, "Mybardataset")

        bardataset.setColor(Color.GREEN, 150)
        val bardata= BarData(bardataset)
        bardata.barWidth=0.2f

        chart1.run(){
            setDrawBarShadow(false)
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