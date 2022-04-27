package com.example.test_project_1.join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.test_project_1.R
import com.example.test_project_1.Retro
import com.example.test_project_1.login.Login
import com.example.test_project_1.login.LoginPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_page)

        var sexCheck: RadioGroup = findViewById(R.id.sexCheck)
        var join_btn: Button = findViewById(R.id.join_btn)
        var check_btn: Button = findViewById(R.id.check_btn)
        var id: EditText = findViewById(R.id.id)
        var password1: EditText = findViewById(R.id.password1)
        var password2: EditText = findViewById(R.id.password2)
        var age: EditText = findViewById(R.id.age)
        var height: EditText = findViewById(R.id.height)
        var weight: EditText = findViewById(R.id.weight)

        var retro = Retro()
        var retrofit = retro.retrofit

        var joinService = retrofit.create(JoinService::class.java)
        var idCheck = retrofit.create(IdCheck::class.java)

        var isIdChecked = 0
        lateinit var checkedId: String

        var sex = "M"
        sexCheck.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.male -> sex = "M"
                R.id.female -> sex= "F"
            }
        }

        check_btn.setOnClickListener {
            var idBlank = id.text.toString().replace(" ", "").equals("")
            if(idBlank){
                Toast.makeText(applicationContext, "ID 입력해", Toast.LENGTH_SHORT).show()
            }
            else{
                var id = id.text.toString()
                idCheck.idCheck(id).enqueue(object:Callback<Join>{
                    override fun onResponse(call: Call<Join>, response: Response<Join>) {
                        var result = response.body()
                        if(result?.code == "0000"){
                            Toast.makeText(applicationContext, "사용 가능한 ID", Toast.LENGTH_SHORT).show()
                            isIdChecked = 1
                            checkedId = id
                        }
                        else{
                            Toast.makeText(applicationContext, "이미 존재하는 ID", Toast.LENGTH_SHORT).show()
                            isIdChecked = 0
                        }
                    }

                    override fun onFailure(call: Call<Join>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }

        join_btn.setOnClickListener {
            var idBlank = id.text.toString().replace(" ", "").equals("")
            var passwordBlank1 = password1.text.toString().replace(" ", "").equals("")
            var passwordBlank2 = password2.text.toString().replace(" ", "").equals("")
            var ageBlank = age.text.toString().replace(" ", "").equals("")
            var heightBlank = height.text.toString().replace(" ", "").equals("")
            var weightBlank = weight.text.toString().replace(" ", "").equals("")


            if(idBlank || passwordBlank1 || passwordBlank2 || ageBlank || heightBlank || weightBlank){
                Toast.makeText(applicationContext, "빈칸 있다", Toast.LENGTH_SHORT).show()
            }else{
                var id = id.text.toString()
                var password = password1.text.toString()
                var password2 = password2.text.toString()
                var age = Integer.parseInt(age.text.toString())
                var height = Integer.parseInt(height.text.toString())
                var weight = Integer.parseInt(weight.text.toString())

                if(isIdChecked == 0 || checkedId != id){
                    Toast.makeText(applicationContext, "ID 중복 체크해", Toast.LENGTH_SHORT).show()
                }else{
                    if(password != password2){
                        Toast.makeText(applicationContext, "패스워드 확인해", Toast.LENGTH_SHORT).show()
                    }else{
                        joinService.requestJoin(id, password, age, sex, height, weight).enqueue(object:Callback<Join>{
                            override fun onResponse(call: Call<Join>, response: Response<Join>) {
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

                            override fun onFailure(call: Call<Join>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        }
    }
}