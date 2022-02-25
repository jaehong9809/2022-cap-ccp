package com.example.test_project_1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.test_project_1.calrecy.CalendarAdapter
import com.example.test_project_1.calrecy.CalendarDateModel
import com.example.test_project_1.databinding.CalendarItemBinding
import com.example.test_project_1.foodrecy.Foodmodel
import com.example.test_project_1.foodrecy.RecyclerAdapter
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

class CalendarPage : Fragment() {
    private val cal = Calendar.getInstance()
    private val currentDate = Calendar.getInstance()
    private lateinit var adapter: CalendarAdapter
    private val date = ArrayList<Date>()
    private val calList = ArrayList<CalendarDateModel>()
    private val sdf = SimpleDateFormat("yyyy년 MM월")
    private val sdf2 = SimpleDateFormat("yyyyMMdd")

    private var daynum: Int = 0

    private lateinit var icpre: ImageView
    private lateinit var icnext: ImageView
    private lateinit var calmonth: TextView
    private lateinit var todaykcal: TextView
    private lateinit var goalkcal: TextView
    private lateinit var camerabt: Button

    private lateinit var maxbar: View
    private lateinit var todaybar: View

    private var todayTotal: Int = 0
    private var maxkcal: Int = 2500

    private var maxwidth: Int = 0
    private var barwidth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar_page, container, false)

        val calrecyview = view.findViewById<RecyclerView>(R.id.recy_date)
        val foodrecyview = view.findViewById<RecyclerView>(R.id.recyview)

        var mDatas: ArrayList<Foodmodel> = requireActivity().intent!!.extras!!.get("DataList") as ArrayList<Foodmodel>

        daynum = currentDate.get(Calendar.YEAR) * 10000 + (currentDate.get(Calendar.MONTH)+1) * 100 + currentDate.get(Calendar.DAY_OF_MONTH)

        icpre = view.findViewById(R.id.cal_pre)
        icnext = view.findViewById(R.id.cal_next)
        calmonth = view.findViewById(R.id.cal_month)
        todaykcal = view.findViewById(R.id.todaykcal)
        goalkcal = view.findViewById(R.id.goalkcal)
        camerabt = view.findViewById(R.id.camera_btn)

        maxbar = view.findViewById(R.id.maxbar)
        todaybar = view.findViewById(R.id.todaybar)

        setUpAdapter(mDatas, calrecyview)
        setUpClickListener(calmonth)
        setUpCalendar(calmonth)

        camerabt.setOnClickListener {
            var intent = Intent(requireContext(), CameraPage::class.java)
            startActivity(intent)
        }


        val recyadapter= RecyclerAdapter(requireContext(), mDatas)
        foodrecyview.adapter=recyadapter
        val mLayoutManager = LinearLayoutManager(context)
        foodrecyview.layoutManager = mLayoutManager
        foodrecyview.setHasFixedSize(true)

        return view
    }

    private fun setUpClickListener(calmonth: TextView) {
        icnext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar(calmonth)
        }
        icpre.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar(calmonth)
            else
                setUpCalendar(calmonth)
        }
    }

    private fun setUpAdapter(mDatas: ArrayList<Foodmodel>, recyclerView: RecyclerView) {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calList.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            calList.forEach {
                if(it.isSelected) {
                    daynum = sdf2.format(it.data).toString().toInt()
                    goalkcal.setText(daynum.toString())
                }
            }
            adapter.setData(calList)
            setbar(mDatas)
        }

        recyclerView.adapter = adapter
    }

    private fun setUpCalendar(calmonth: TextView) {
        val calendarList = ArrayList<CalendarDateModel>()
        calmonth.text = sdf.format(cal.time)

        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        date.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (date.size < maxDaysInMonth) {
            date.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calList.clear()
        calList.addAll(calendarList)
        adapter.setData(calendarList)
    }

    fun todaysum(mDatas: ArrayList<Foodmodel>, daynum: Int): Int {
        var total = 0
        mDatas.forEach {
            if((it.food_time / 10) == daynum)
                total += it.food_kcal
        }
        return total
    }

    fun kcalbar(width: Int, todayTotal: Int, maxkcal: Int): Int {
        var rate = (todayTotal*100) / maxkcal
        if(rate > 100) rate = 100
        var ratewidth = (width * rate) / 100

        return ratewidth
    }

    fun setbar(mDatas: ArrayList<Foodmodel>) {
        todayTotal = todaysum(mDatas, daynum)

        todaykcal.setText(todayTotal.toString())
        goalkcal.setText(maxkcal.toString())
        // 진행 바
        maxbar.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                maxwidth = maxbar.width
                barwidth = kcalbar(maxwidth, todayTotal, maxkcal)
                val params: FrameLayout.LayoutParams = todaybar.getLayoutParams() as FrameLayout.LayoutParams
                params.width = barwidth
                todaybar.setLayoutParams(params)
            }
        })
        if(todayTotal > maxkcal) {
            todaybar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
        else {
            todaybar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange))
        }
    }
}