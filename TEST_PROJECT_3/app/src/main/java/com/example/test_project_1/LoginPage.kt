package com.example.test_project_1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.test_project_1.login.Login
import com.example.test_project_1.login.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPage : AppCompatActivity() {

    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)

        var join_btn: Button = findViewById(R.id.join_btn)
        var login_btn: Button = findViewById(R.id.login_btn)
        var ID: EditText = findViewById(R.id.ID)
        var PW: EditText = findViewById(R.id.PW)

        join_btn.setOnClickListener {
            var intent = Intent(applicationContext, JoinPage::class.java)
            startActivity(intent)
        }

        var retro = Retro()
        var retrofit = retro.retrofit

        var loginService = retrofit.create(LoginService::class.java)

        login_btn.setOnClickListener {
            var textId = ID.text.toString()
            var textPw = PW.text.toString()

            loginService.requestLogin(textId, textPw).enqueue(object: Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    var login = response.body()
                    if(login?.code == "0000"){
                        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                        var intent= Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("textId", textId)
                        intent.putExtra("sex", login?.sex)
                        intent.putExtra("weight", login?.weight)
                        intent.putExtra("height", login?.height)
                        intent.putExtra("age", login?.age)
                        intent.putExtra("img", login?.img)

                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, "ID 또는 PW 틀렸다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                }

            })
            /*var intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)*/
        }
    }

    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList: MutableList<String> = mutableListOf()
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission)
            }
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(),
                PERMISSIONS_REQUEST
            )
            return false
        }
        return true
    }
}