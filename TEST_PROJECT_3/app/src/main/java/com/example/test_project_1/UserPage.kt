package com.example.test_project_1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
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
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
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
    lateinit var advtv1:TextView
    lateinit var advtv2:TextView
    lateinit var advtv3:TextView
    lateinit var advtv4:TextView
    lateinit var advimv:ImageView
    lateinit var userimg:ImageView
    var standardcal=0
    var standardcarbo=0
    var standardpro=0
    var standardfat=0
    var day30totalcal=0
    var day30totalcarbo=0
    var day30totalpro=0
    var day30totalfat=0
    var realdtcnt=0
    var tmp_arr15=Array(15, {Array(5, {"0"})})
    var tmp_arr30=Array(30, {Array(5, {"0"})})

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
        advtv1=view.findViewById(R.id.advtv1)
        advtv2=view.findViewById(R.id.advtv2)
        advtv3=view.findViewById(R.id.advtv3)
        advtv4=view.findViewById(R.id.advtv4)

        advimv=view.findViewById(R.id.advimv1)
        modifybt=view.findViewById(R.id.modifybt)
        userimg=view.findViewById(R.id.userimg)

        class MonthlyMarkerView : MarkerView {
            lateinit var tvContent: TextView // marker
            constructor(context: Context?, layoutResource: Int) : super(context, layoutResource)
            {
                tvContent = findViewById(R.id.test_marker_view)
            } // draw override를 사용해 marker의 위치 조정 (bar의 상단 중앙)
            override fun draw(canvas: Canvas?) {
                canvas!!.translate(-(width / 2).toFloat(), -height.toFloat() )
                super.draw(canvas)
            } // entry를 content의 텍스트에 지정
            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                val ind= e?.x?.toInt()!! -1

                tvContent.text = "칼로리 :"+e?.y.toString()+"\n탄수화물:"+tmp_arr30[ind][2]+"\n단백질 :"+tmp_arr30[ind][3]+
                        "\n지 방 :"+tmp_arr30[ind][3]
                super.refreshContent(e, highlight)
            }

        }

        var userone=userData(id,user_age, user_weight, user_sex, 1700, user_height)
        var qq=((userone.weight)/((userone.height*0.01)*(userone.height*0.01))).toInt()
        when(qq){
            in 0..18 ->daesatv.text="저체중"
            in 18..23 ->daesatv.text="정상체중"
            in 23..25 ->daesatv.text="과체중"
            in 25..100 ->daesatv.text="비만"
        }
        agetv.text=Integer.toString(userone.age)
        weight.text=Integer.toString(userone.weight)
        sextv.text=userone.sex
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
                    tmp_arr15=result.day15
                    var day30 = result.day30
                    tmp_arr30=result.day30

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
                    val marker1=MonthlyMarkerView(context, layoutResource = R.layout.tvcontent)
                    chart1.marker=marker1

                    var entries30 =ArrayList<dayCalorie>()    //30일짜리 데이터 셋
                    index=0

                    for (i in day30){
                        index++
                        var str=i[0].substring(4,6)+"/"+i[0].substring(6, 8)
                        var calorie=i[1].toInt()
                        var carbo=i[2].toInt()
                        var protein=i[3].toInt()
                        var fat=i[4].toInt()
                        if(calorie!=0)realdtcnt++
                        day30totalcal+=calorie
                        day30totalcarbo+=carbo
                        day30totalpro+=protein
                        day30totalfat+=fat
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
                    val marker2=MonthlyMarkerView(context, layoutResource = R.layout.tvcontent)
                    chart2.marker=marker2
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
                        standardpro=when(user_age){
                            in 0..5 ->25
                            in 6..8 ->35
                            in 9..11->50
                            in 12..14->60
                            in 15..18->65
                            in 19..29->65
                            in 30..49->65
                            in 50..64->65
                            in 65..74->60
                            else ->60
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
                        standardpro=when(user_age){
                            in 0..5 ->20
                            in 6..8 ->30
                            in 9..11->40
                            in 12..14->50
                            in 15..18->55
                            in 19..29->50
                            in 30..49->50
                            in 50..64->50
                            in 65..74->50
                            else ->50
                        }
                    }
                    var avgday30cal=0
                    var avgday30carbo=0
                    var avgday30pro=0
                    var avgday30fat=0

                    if(realdtcnt!=0){
                        avgday30cal= day30totalcal/realdtcnt
                        avgday30carbo=day30totalcarbo/realdtcnt
                        avgday30pro=day30totalpro/realdtcnt
                        avgday30fat=day30totalfat/realdtcnt
                    }
                    if(avgday30cal>standardcal+10){                 //기준+1000 보다 큼 -> 잘못됨
                        advtv1.text="식단 칼로리:${avgday30cal}, 권장:${standardcal} \n추천: 칼로리 줄이기"
                        advimv.setImageResource(R.drawable.star_bad)
                    }
                    else if((avgday30cal>standardcal-10)&&(avgday30cal<standardcal+10)){    //기준-1000 < <기준 +1000 사이 -> good
                        advtv1.text="식단 칼로리:${avgday30cal}, 권장:${standardcal} \n추천: 칼로리 유지"
                        advimv.setImageResource(R.drawable.star_good)
                    }
                    else if(avgday30cal<standardcal-10){                //기준 -1000보다 작음 -> 아주 굳
                        advtv1.text="식단 칼로리:${avgday30cal}, 권장:${standardcal} \n추천: 칼로리 늘리기"
                        advimv.setImageResource(R.drawable.star_verygood)
                    }
                    standardcarbo=when(user_age){
                        in 0..5 -> 60
                        in 6..11 -> 90
                        else -> 130
                    }
                    if(avgday30carbo>standardcarbo+10){                 //기준+1000 보다 큼 -> 잘못됨
                        advtv2.text="식단 탄수화물:${avgday30carbo}, 권장:${standardcarbo} \n추천: 탄수화물 줄이기"
                    }
                    else if((avgday30carbo>standardcarbo-10)&&(avgday30carbo<standardcarbo+10)){    //기준-1000 < <기준 +1000 사이 -> good
                        advtv2.text="식단 탄수화물:${avgday30carbo}, 권장:${standardcarbo} \n추천: 탄수화물 유지 "
                    }
                    else if(avgday30carbo<standardcarbo-10){                //기준 -1000보다 작음 -> 아주 굳
                        advtv2.text="식단 탄수화물:${avgday30carbo}, 권장:${standardcarbo} \n추천: 탄수화물 늘리기"
                    }

                    if(avgday30pro>standardpro+10){                 //기준+1000 보다 큼 -> 잘못됨
                        advtv3.text="식단 단백질:${avgday30pro}, 권장:${standardpro} \n추천: 단백질 줄이기"
                    }
                    else if((avgday30pro>standardpro-10)&&(avgday30pro<standardpro+10)){    //기준-1000 < <기준 +1000 사이 -> good
                        advtv3.text="식단 단백질:${avgday30pro}, 권장:${standardpro} \n추천: 단백질 유지"
                    }
                    else if(avgday30pro<standardpro-10){                //기준 -1000보다 작음 -> 아주 굳
                        advtv3.text="식단 단백질:${avgday30pro}, 권장:${standardpro} \n추천: 단백질 늘리기"
                    }
                    standardfat=51
                    if(avgday30fat>standardfat+10){                 //기준+1000 보다 큼 -> 잘못됨
                        advtv4.text="식단 지방:${avgday30fat}, 권장:${standardfat} \n추천: 지방 줄이기"
                    }
                    else if((avgday30fat>standardfat-10)&&(avgday30fat<standardfat+10)){    //기준-1000 < <기준 +1000 사이 -> good
                        advtv4.text="식단 지방:${avgday30fat}, 권장:${standardfat} \n추천: 유지"
                    }
                    else if(avgday30fat<standardfat-10){                //기준 -1000보다 작음 -> 아주 굳
                        advtv4.text="식단 지방:${avgday30fat}, 권장:${standardfat} \n추천: 유지"
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
            when(qq){
                in 0..18 ->daesatv.text="저체중"
                in 18..23 ->daesatv.text="정상"
                in 23..25 ->daesatv.text="과체중"
                in 25..100 ->daesatv.text="비만"
            }
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
        ylaxis.axisMaximum=(max+4000).toFloat()
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