package com.example.test_project_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import java.util.*
import kotlin.collections.ArrayList


class InfoPage : Fragment() {
    data class userFood(var rice:Int, var Acornjellu:Int, var redbean:Int, var ramen:Int, var japchae:Int)
    data class foodcount(var rice:Int, var Acornjellu:Int, var redbean:Int, var ramen:Int, var japchae:Int)
    lateinit var sp1: Spinner
    lateinit var sp2:Spinner
    lateinit var sp3:Spinner
    lateinit var sp4:Spinner
    lateinit var sp5: Spinner
    lateinit var sp6:Spinner
    lateinit var sp7:Spinner
    lateinit var sp8:Spinner
    lateinit var piechart1:PieChart
    lateinit var piechart2:PieChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_page, container, false)

        sp1=view.findViewById(R.id.sp1)
        sp2=view.findViewById(R.id.sp2)
        sp3=view.findViewById(R.id.sp3)
        sp4=view.findViewById(R.id.sp4)
        sp5=view.findViewById(R.id.sp5)
        sp6=view.findViewById(R.id.sp6)
        sp7=view.findViewById(R.id.sp7)
        sp8=view.findViewById(R.id.sp8)

        piechart1=view.findViewById(R.id.pichart1)
        piechart2=view.findViewById(R.id.pichart2)

        var sexlist= mutableListOf<String>("   남", "   여")
        var ada1:ArrayAdapter<String>
        ada1= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sexlist)     //성별 스피너
        sp1.adapter=ada1
        sp5.adapter=ada1


        var weightlist= mutableListOf<String>()
        for(i in 30..120){
            weightlist.add("   "+i.toString())
        }
        var ada2:ArrayAdapter<String>
        ada2= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, weightlist)     //몸무게스피너
        sp2.adapter=ada2
        sp6.adapter=ada2

        var heightlist= mutableListOf<String>()
        var ada3:ArrayAdapter<String>
        for(i in 100..200){
            heightlist.add(" "+i.toString())
        }
        ada3=ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, heightlist)      // 키 스피너
        sp3.adapter=ada3
        sp7.adapter=ada3

        var agelist= mutableListOf<String>()
        for(i in 1..100){
            agelist.add("   "+i.toString())
        }
        var ada4: ArrayAdapter<String>
        ada4= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, agelist)        //나이 스피너
        sp4.adapter=ada4
        sp8.adapter=ada4

        var usersfood= mutableListOf<userFood>()
        val random= Random()

        for (i in 1..10){   // 사용자가 먹은 음식 횟수 랜덤 저장
            var tmpuser=userFood(random.nextInt(10), random.nextInt(10), random.nextInt(10), random.nextInt(10),random.nextInt(10))
            usersfood.add(tmpuser)
        }

        var foodcnt=foodcount(0,0,0,0,0)

        for( i in usersfood){       //사용자들이 먹은 음식 횟수 총합
            foodcnt.rice+=i.rice
            foodcnt.Acornjellu=i.Acornjellu
            foodcnt.redbean=i.redbean
            foodcnt.ramen=i.ramen
            foodcnt.japchae=i.japchae
        }
        val entries:ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry((foodcnt.rice).toFloat(), "RICE"))
        entries.add(PieEntry((foodcnt.Acornjellu).toFloat(), "ACORNJELLY"))
        entries.add(PieEntry((foodcnt.redbean).toFloat(), "REDBEAN"))
        entries.add(PieEntry((foodcnt.ramen).toFloat(), "RAMEN"))
        entries.add(PieEntry((foodcnt.japchae).toFloat(), "JAPCHAE"))

        makepiechart(piechart1, entries)
        makepiechart(piechart2, entries)

        return view
    }

    fun makepiechart(piechart:PieChart, entries:ArrayList<PieEntry>){
        piechart.setUsePercentValues(true)
        val colorsitems=ArrayList<Int>()
        for(c in ColorTemplate.VORDIPLOM_COLORS) colorsitems.add(c)
        for(c in ColorTemplate.JOYFUL_COLORS) colorsitems.add(c)
        for(c in COLORFUL_COLORS) colorsitems.add(c)
        for(c in ColorTemplate.LIBERTY_COLORS) colorsitems.add(c)
        for(c in ColorTemplate.PASTEL_COLORS) colorsitems.add(c)
        colorsitems.add(ColorTemplate.getHoloBlue())

        val pieDataset=PieDataSet(entries, "")
        pieDataset.apply {
            colors=colorsitems
            valueTextColor= Color.BLACK
            valueTextSize=16f
        }
        val pieData=PieData(pieDataset)
        piechart.apply{
            data=pieData
            description.isEnabled=false
            isRotationEnabled=false
            isDrawHoleEnabled=false

            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }
}