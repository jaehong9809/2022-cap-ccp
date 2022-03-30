package com.example.test_project_1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class InfoPage : Fragment() {
    data class userFood(
        var rice: Int,
        var Acornjellu: Int,
        var redbean: Int,
        var ramen: Int,
        var japchae: Int
    )

    data class foodcount(
        var rice: Int,
        var Acornjellu: Int,
        var redbean: Int,
        var ramen: Int,
        var japchae: Int
    )

    lateinit var sp1: Spinner
    lateinit var sp2: Spinner
    lateinit var sp3: Spinner
    lateinit var sp4: Spinner
    lateinit var piechart1: PieChart
    lateinit var barchart1: BarChart
    lateinit var linechart1: LineChart
    lateinit var monthavgcal: Array<Array<String>>
    lateinit var agecalavglist: Array<String>
    var typemsg = Array<String>(4, { "" })
    var retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.35.118:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var dataInfo = retrofit.create(DataInfo::class.java)

    var retrofit2 = Retrofit.Builder()
        .baseUrl("http://192.168.35.118:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var dataInfo2 = retrofit2.create(DataInfo2::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_page, container, false)

        sp1 = view.findViewById(R.id.sp1)
        sp2 = view.findViewById(R.id.sp2)
        sp3 = view.findViewById(R.id.sp3)
        sp4 = view.findViewById(R.id.sp4)

        piechart1 = view.findViewById(R.id.pichart1)
        barchart1 = view.findViewById(R.id.barchart1)
        linechart1 = view.findViewById(R.id.linechart1)

        var sex = requireActivity().intent!!.extras!!.get("sex") as String
        var weight = requireActivity().intent!!.extras!!.get("weight") as Int
        var height = requireActivity().intent!!.extras!!.get("height") as Int
        var age = requireActivity().intent!!.extras!!.get("age") as Int

        var rweight:Int
        var rheight:Int
        var rage:Int

        var sexlist = mutableListOf<String>("  남", "  여")
        var ada1: ArrayAdapter<String>
        ada1 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            sexlist
        )     //성별 스피너
        sp1.adapter = ada1


        var weightlist = mutableListOf<String>()
        for (i in 30..120 step 10) {
            weightlist.add(i.toString())
        }
        var ada2: ArrayAdapter<String>
        ada2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            weightlist
        )     //몸무게스피너
        sp2.adapter = ada2


        var heightlist = mutableListOf<String>()
        var ada3: ArrayAdapter<String>
        for (i in 100..200 step 10) {
            heightlist.add(i.toString())
        }
        ada3 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            heightlist
        )      // 키 스피너
        sp3.adapter = ada3


        var agelist = mutableListOf<String>()
        for (i in 10..100 step 10) {
            agelist.add(i.toString())
        }
        var ada4: ArrayAdapter<String>
        ada4 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            agelist
        )        //나이 스피너
        sp4.adapter = ada4

        if (sex.equals('M')) {
            sp1.setSelection(0)
        }
        else{
            sp1.setSelection(1)
        }
        if((weight/10)-2<0){
            sp2.setSelection(0)
            rweight=30
        }
        else{
            sp2.setSelection((weight/10)-2)
            rweight=(weight/10)*10
        }
        if((height/10)-10<0){
            sp3.setSelection(0)
            rheight=100
        }
        else{
            sp3.setSelection((height/10)-10)
            rheight=(height/10)*10
        }
        if((age/10)-1<0){
            sp4.setSelection(0)
            rage=10
        }
        else{
            sp4.setSelection((age/10)-1)
            rage=(age/10)*10
        }

        sp1.setSelection(1)
        sp2.setSelection(1)
        sp3.setSelection(1)
        sp4.setSelection(1)
        dataInfo2.search(sex, rheight, rweight, rage).enqueue(object : Callback<Data2>{
            override fun onResponse(call: Call<Data2>, response: Response<Data2>) {
                var result = response.body()
                if(result?.code =="0000"){
                    var foods = result.info
                    val entries: ArrayList<PieEntry> = ArrayList()
                    for (i in foods){
                        entries.add(PieEntry((i[1].toInt()).toFloat(), i[0]))
                    }
                    makepiechart(piechart1, entries)
                }
                else{
                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Data2>, t: Throwable) {
                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
            }

        })


        //------------------------통신-----------------------------------by heegang
        dataInfo.searchData().enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                var result = response.body()
                if (result?.code == "0000") {
                    monthavgcal = result.monthavgcal
                    agecalavglist = result.agecalavglist


                } else
                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()



                sp1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        dataInfo2.search(sp1.selectedItem.toString(), sp3.selectedItem.toString().toInt(), sp2.selectedItem.toString().toInt(), sp4.selectedItem.toString().toInt()).enqueue(object : Callback<Data2>{
                            override fun onResponse(call: Call<Data2>, response: Response<Data2>) {
                                var result = response.body()
                                if(result?.code =="0000"){
                                    var foods = result.info
                                    val entries: ArrayList<PieEntry> = ArrayList()
                                    for (i in foods){
                                        entries.add(PieEntry((i[1].toInt()).toFloat(), i[0]))
                                    }
                                    makepiechart(piechart1, entries)
                                }
                                else{
                                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Data2>, t: Throwable) {
                                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                sp2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        dataInfo2.search(sp1.selectedItem.toString(), sp3.selectedItem.toString().toInt(), sp2.selectedItem.toString().toInt(), sp4.selectedItem.toString().toInt()).enqueue(object : Callback<Data2>{
                            override fun onResponse(call: Call<Data2>, response: Response<Data2>) {
                                var result = response.body()
                                if(result?.code =="0000"){
                                    var foods = result.info
                                    val entries: ArrayList<PieEntry> = ArrayList()
                                    for (i in foods){
                                        entries.add(PieEntry((i[1].toInt()).toFloat(), i[0]))
                                    }
                                    makepiechart(piechart1, entries)
                                }
                                else{
                                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Data2>, t: Throwable) {
                                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                sp3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        dataInfo2.search(sp1.selectedItem.toString(), sp3.selectedItem.toString().toInt(), sp2.selectedItem.toString().toInt(), sp4.selectedItem.toString().toInt()).enqueue(object : Callback<Data2>{
                            override fun onResponse(call: Call<Data2>, response: Response<Data2>) {
                                var result = response.body()
                                if(result?.code =="0000"){
                                    var foods = result.info
                                    val entries: ArrayList<PieEntry> = ArrayList()
                                    for (i in foods){
                                        entries.add(PieEntry((i[1].toInt()).toFloat(), i[0]))
                                    }
                                    makepiechart(piechart1, entries)
                                }
                                else{
                                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Data2>, t: Throwable) {
                                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                sp4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        dataInfo2.search(sp1.selectedItem.toString(), sp3.selectedItem.toString().toInt(), sp2.selectedItem.toString().toInt(), sp4.selectedItem.toString().toInt()).enqueue(object : Callback<Data2>{
                            override fun onResponse(call: Call<Data2>, response: Response<Data2>) {
                                var result = response.body()
                                if(result?.code =="0000"){
                                    var foods = result.info
                                    val entries: ArrayList<PieEntry> = ArrayList()
                                    for (i in foods){
                                        entries.add(PieEntry((i[1].toInt()).toFloat(), i[0]))
                                    }
                                    makepiechart(piechart1, entries)
                                }
                                else{
                                    Toast.makeText(getActivity(), "없어", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Data2>, t: Throwable) {
                                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }

                var usersfood = mutableListOf<userFood>()
                val random = Random()

                for (i in 1..10) {   // 사용자가 먹은 음식 횟수 랜덤 저장
                    var tmpuser = userFood(
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10)
                    )
                    usersfood.add(tmpuser)
                }

                var foodcnt = foodcount(0, 0, 0, 0, 0)

                for (i in usersfood) {       //사용자들이 먹은 음식 횟수 총합
                    foodcnt.rice += i.rice
                    foodcnt.Acornjellu = i.Acornjellu
                    foodcnt.redbean = i.redbean
                    foodcnt.ramen = i.ramen
                    foodcnt.japchae = i.japchae
                }
                /* val entries: ArrayList<PieEntry> = ArrayList()
                 entries.add(PieEntry((foodcnt.rice).toFloat(), "RICE"))
                 entries.add(PieEntry((foodcnt.Acornjellu).toFloat(), "ACORNJELLY"))
                 entries.add(PieEntry((foodcnt.redbean).toFloat(), "REDBEAN"))
                 entries.add(PieEntry((foodcnt.ramen).toFloat(), "RAMEN"))
                 entries.add(PieEntry((foodcnt.japchae).toFloat(), "JAPCHAE"))

                 makepiechart(piechart1, entries)*/

                val entriesa = ArrayList<BarEntry>()
                entriesa.add(BarEntry(0f, 0f))
                /*entriesa.add(BarEntry(1f, 1000f))
                entriesa.add(BarEntry(2f, 1200f))
                entriesa.add(BarEntry(3f, 900f))       //0인덱스는 뺴야할듯
                entriesa.add(BarEntry(4f, 1400f))
                entriesa.add(BarEntry(5f, 1000f))
                entriesa.add(BarEntry(6f, 1200f))
                entriesa.add(BarEntry(7f, 1300f))
                entriesa.add(BarEntry(8f, 800f))
                 */
                for (i in 1..8) {
                    entriesa.add(BarEntry(i.toFloat(), (agecalavglist[i - 1].toInt()).toFloat()))
                }


                makechart(barchart1, entriesa)

                val entriesq = ArrayList<Entry>()
                /* entriesq.add(Entry(0f, 900f))
                 entriesq.add(Entry(1f, 1200f))
                 entriesq.add(Entry(2f, 1100f))
                 entriesq.add(Entry(3f, 1500f))       //0인덱스는 뺴야할듯
                 entriesq.add(Entry(4f, 1300f))
                 entriesq.add(Entry(5f, 800f))
                 entriesq.add(Entry(6f, 2000f))
                 entriesq.add(Entry(7f, 1200f))
                 entriesq.add(Entry(8f, 1500f))
                 entriesq.add(Entry(9f, 400f))
                 entriesq.add(Entry(10f, 1400f))
                 entriesq.add(Entry(11f, 1000f))
                 */
                for (i in 0..11) {
                    entriesq.add(Entry(i.toFloat(), (monthavgcal[i][1].toInt()).toFloat()))
                }


                makelinechart(linechart1, entriesq)

            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(getActivity(), "통신 실패", Toast.LENGTH_SHORT).show()
            }

        })

        //-----------------------통신끝----------------------------------------------

        /*typemsg[0] = sp1.selectedItem.toString()
        typemsg[1] = sp2.selectedItem.toString()
        typemsg[2] = sp3.selectedItem.toString()
        typemsg[3] = sp4.selectedItem.toString()*/



        return view
    }

    fun makepiechart(piechart: PieChart, entries: ArrayList<PieEntry>) {
        piechart.setUsePercentValues(true)
        val colorsitems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsitems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsitems.add(c)
        for (c in COLORFUL_COLORS) colorsitems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsitems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsitems.add(c)
        colorsitems.add(ColorTemplate.getHoloBlue())

        val pieDataset = PieDataSet(entries, "")
        pieDataset.apply {
            colors = colorsitems
            valueTextColor = Color.BLACK
            valueTextSize = 16f
        }
        val pieData = PieData(pieDataset)
        piechart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            isDrawHoleEnabled = false

            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }

    fun makechart(chart1: BarChart, entries: ArrayList<BarEntry>) {
        val age = ArrayList<String>()
        age.add("")
        for (i in 0..7) {
            age.add((i + 1).toString() + "0대")
        }
        val bardataset = BarDataSet(entries, "Mybardataset")
        bardataset.valueTextSize = 12f
        bardataset.setColor(Color.CYAN, 150)
        val bardata = BarData(bardataset)
        bardata.barWidth = 0.2f

        chart1.run() {
            setDrawBarShadow(false)
            legend.isEnabled = false
        }
        val xaxis = chart1.xAxis
        xaxis.setDrawLabels(true)



        xaxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return age[value.toInt()]
            }
        }

        xaxis.textColor = Color.BLACK
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.setDrawLabels(true)
        xaxis.setDrawAxisLine(true)
        xaxis.axisMinimum = 0f
        xaxis.labelCount = 8

        var max: Int = 0
        var min: Int = 10000
        for (i in entries) {
            if (i.x == 0f) continue

            if (i.y > max)
                max = i.y.toInt()
            if (i.y < min)
                min = i.y.toInt()
        }
        if ((min - 100) < 0)
            min = 0
        val ylaxis = chart1.axisLeft
        ylaxis.axisMaximum = (max + 100).toFloat()
        ylaxis.axisMinimum = (min - 100).toFloat()

        ylaxis.setDrawAxisLine(false)
        ylaxis.setDrawGridLines(false)

        val yraxis = chart1.axisRight
        yraxis.setDrawLabels(false)
        yraxis.setDrawAxisLine(false)
        yraxis.setDrawGridLines(false)
        chart1.setScaleEnabled(false)
        chart1.setPinchZoom(false)
        chart1.description = null
        xaxis.setDrawGridLines(false)

        chart1.animateXY(0, 800)
        chart1.data = bardata
        chart1.invalidate()
    }

    fun makelinechart(chart1: LineChart, entries: ArrayList<Entry>) {
        val age = ArrayList<String>()
        for (i in monthavgcal) {
            var str = i[0].substring(4 until 6)
            age.add((str.toInt()).toString() + "월")
        }


        val bardataset = LineDataSet(entries, "Mybardataset")

        bardataset.setColor(Color.CYAN, 150)
        bardataset.setDrawCircleHole(false)
        bardataset.lineWidth = 2f
        bardataset.circleRadius = 5f
        bardataset.valueTextSize = 12f
        val bardata = LineData(bardataset)


        chart1.run() {

            legend.isEnabled = false
        }
        val xaxis = chart1.xAxis
        xaxis.setDrawLabels(true)



        xaxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return age[value.toInt()]
            }
        }

        xaxis.textColor = Color.BLACK
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.setDrawLabels(true)
        xaxis.setDrawAxisLine(true)
        xaxis.axisMinimum = 0f
        xaxis.labelCount = 8

        var max: Int = 0
        var min: Int = 10000
        for (i in entries) {

            if (i.y > max)
                max = i.y.toInt()
            if (i.y < min)
                min = i.y.toInt()
        }
        if ((min - 100) < 0)
            min = 0
        val ylaxis = chart1.axisLeft
        ylaxis.axisMaximum = (max + 100).toFloat()
        ylaxis.axisMinimum = (min - 100).toFloat()

        ylaxis.setDrawAxisLine(false)
        ylaxis.setDrawGridLines(false)

        val yraxis = chart1.axisRight
        yraxis.setDrawLabels(false)
        yraxis.setDrawAxisLine(false)
        yraxis.setDrawGridLines(false)
        chart1.setScaleEnabled(false)
        chart1.setPinchZoom(false)
        chart1.description = null
        xaxis.setDrawGridLines(false)

        chart1.animateXY(0, 800)
        chart1.data = bardata
        chart1.invalidate()
    }
}