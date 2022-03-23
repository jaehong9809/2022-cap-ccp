package com.example.test_project_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.test_project_1.join.JoinService
import com.example.test_project_1.login.Login
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_page)

        var sexCheck: RadioGroup = findViewById(R.id.sexCheck)
        var join_btn: Button = findViewById(R.id.join_btn)
        var username: EditText = findViewById(R.id.username)
        var password1: EditText = findViewById(R.id.password1)
        var age: EditText = findViewById(R.id.age)
        var height: EditText = findViewById(R.id.height)
        var weight: EditText = findViewById(R.id.weight)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.35.118:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var joinService = retrofit.create(JoinService::class.java)

        lateinit var sex: String
        sexCheck.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.male -> sex = "M"
                R.id.female -> sex= "F"
            }
        }

        join_btn.setOnClickListener {
            var username = username.text.toString()
            var password = password1.text.toString()
            var age = Integer.parseInt(age.text.toString())
            var height = Integer.parseInt(height.text.toString())
            var weight = Integer.parseInt(weight.text.toString())


            joinService.requestJoin(username, password, age, sex, height, weight).enqueue(object:Callback<Login>{
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    var join = response.body()
                    if (join?.code == "0000"){
                        Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        var intent= Intent(applicationContext, LoginPage::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }
}