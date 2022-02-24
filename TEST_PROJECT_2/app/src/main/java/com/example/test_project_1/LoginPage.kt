package com.example.test_project_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class LoginPage : AppCompatActivity() {
    lateinit var LoginId: EditText
    lateinit var LoginPw: EditText
    lateinit var LoginBt: Button
    lateinit var JoinBt: Button
    lateinit var id:String
    lateinit var pw:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
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
}