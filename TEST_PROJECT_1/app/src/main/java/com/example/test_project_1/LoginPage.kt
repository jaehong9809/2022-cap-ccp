package com.example.test_project_1

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class LoginPage : AppCompatActivity() {
    lateinit var LoginId: EditText
    lateinit var LoginPw: EditText
    lateinit var LoginBt: Button
    lateinit var JoinBt: Button
    lateinit var id:String
    lateinit var pw:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        LoginId=findViewById(R.id.login_id)
        LoginPw=findViewById(R.id.login_pw)
        LoginBt=findViewById(R.id.login_bt)
        JoinBt=findViewById(R.id.join_bt)

        LoginBt.setOnClickListener {
            var intent2= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent2)
        }
        JoinBt.setOnClickListener {
            var intent2= Intent(applicationContext, JoinPage::class.java)
            startActivity(intent2)
        }
    }
    fun loginrequest(userID:String, password:String, username:String="banana"){
        var dialogBuilder=AlertDialog.Builder(this@LoginPage)
        if(username.isEmpty() || password.isEmpty()){
            dialogBuilder.setTitle("알림")
            dialogBuilder.setMessage("빈 칸을 전부 채워주세요.")
            dialogBuilder.setPositiveButton("확인", null)
            dialogBuilder.show()
        }else{
            RetrofitManager.instance.login(userID = userID,  password=password,username=username, completion = {
                    loginResponse, response ->
                when(loginResponse){
                    Loginresponse.FAIL -> {
                        dialogBuilder.setTitle("알림")
                        dialogBuilder.setMessage("로그인 실패")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
                    }

                    Loginresponse.OK -> {
                        dialogBuilder.setTitle("알림")
                        dialogBuilder.setMessage("로그인 성공")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
                    }
                }
            })
        }
    }
}