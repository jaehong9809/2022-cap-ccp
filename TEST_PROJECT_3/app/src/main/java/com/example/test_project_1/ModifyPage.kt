package com.example.test_project_1

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.example.test_project_1.login.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyPage : AppCompatActivity() {
    private val OPEN_GALLERY=1
    lateinit var edt_id: TextView
    lateinit var edt_sex:EditText
    lateinit var edt_height:EditText
    lateinit var edt_weight:EditText
    lateinit var edt_age:EditText
    lateinit var okbt:Button
    lateinit var nobt:Button
    lateinit var imv1:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modify_page)
        edt_id=findViewById(R.id.ed0)
        edt_sex=findViewById(R.id.ed1)
        edt_height=findViewById(R.id.ed2)
        edt_weight=findViewById(R.id.ed3)
        edt_age=findViewById(R.id.ed4)
        okbt=findViewById(R.id.okbt)
        nobt=findViewById(R.id.nobt)
        imv1=findViewById(R.id.imv1)
        edt_id.setText(setid)
        edt_sex.setText(setsex)
        edt_height.setText(setheight.toString())
        edt_weight.setText(setweight.toString())
        edt_age.setText(setage.toString())

        var retro = Retro()
        var retrofit = retro.retrofit
        var modify = retrofit.create(UserMod::class.java)

        imv1.setOnClickListener{
            val intent= Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, OPEN_GALLERY)
        }

        okbt.setOnClickListener {
            setid=edt_id.text.toString()
            setsex=edt_sex.text.toString()
            setheight=edt_height.text.toString().toInt()
            setweight=edt_weight.text.toString().toInt()
            setage=edt_age.text.toString().toInt()

            modify.mod(setid, setsex, setheight, setweight, setage).enqueue(object: Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    var result = response.body()
                    if (result?.code == "0000"){
                        Toast.makeText(applicationContext, "수정 성공", Toast.LENGTH_SHORT).show()
                        var outIntent = Intent(applicationContext, UserPage::class.java)
                        setResult(Activity.RESULT_OK, outIntent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "뭔가 틀림", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
        nobt.setOnClickListener {
            finish()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==OPEN_GALLERY){
                var currentImageUrl: Uri?=data?.data
                try{
                    var uri=data?.data
                    imv1.setImageURI(uri)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }else{
            Log.d("ActivityResult", "somethingwrong")
        }
    }
}