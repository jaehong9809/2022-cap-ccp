package com.example.test_project_1

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CalendarPage : Fragment() {
    lateinit var cv1: MaterialCalendarView
    lateinit var tv1: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar_page, container, false)

        cv1=view.findViewById(R.id.calendar)
        tv1=view.findViewById(R.id.todaydate)
        var camera_btn: Button = view.findViewById(R.id.camera_btn)
        camera_btn.setOnClickListener {
            val intent = Intent(getActivity(), CameraPage::class.java)
            startActivityForResult(intent, 0)
        }

        var cal= Calendar.getInstance()
        var cyear=cal.get(Calendar.YEAR)
        var cmonth=cal.get(Calendar.MONTH)
        var cday=cal.get(Calendar.DAY_OF_MONTH)
        tv1.text=Integer.toString(cyear)+"_"+Integer.toString(cmonth+1)+"_"+Integer.toString(cday)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            var cal = data!!.getStringExtra("cal")
            Toast.makeText(getActivity(), cal, Toast.LENGTH_SHORT).show()
        }
    }
}