package com.example.test_project_1

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat

class CameraPage: AppCompatActivity() {
    lateinit var photoFile: File
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera)



        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = File(
            File("${filesDir}/image").apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            },
            newJpgFileName()
        )
        photoUri = FileProvider.getUriForFile(
            this,
            "com.example.test_project_1.fileprovider",
            photoFile
        )

        takePictureIntent.resolveActivity(packageManager)?.also {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, 0)
        }




    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.35.118:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        if (resultCode == Activity.RESULT_OK) {
            val file = File(photoFile.absolutePath)
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val image = MultipartBody.Part.createFormData("proFile", file.name, requestFile)
            val sdf = SimpleDateFormat("yyyyMMdd")
            var filename = sdf.format(System.currentTimeMillis())
            var picture = retrofit.create(Picture::class.java)
            var intent = intent
            var time = intent.getStringExtra("time") as String
            var sex = MultipartBody.Part.createFormData("sex",intent.getStringExtra("sex") as String)
            var weight = MultipartBody.Part.createFormData("weight",intent.getIntExtra("weight", 0).toString())
            var height = MultipartBody.Part.createFormData("height", intent.getIntExtra("height", 0).toString())
            var age = MultipartBody.Part.createFormData("age", intent.getIntExtra("age", 0).toString())
            filename += time
            val date = MultipartBody.Part.createFormData("date", filename)
            val id = MultipartBody.Part.createFormData("id", intent.getStringExtra("textId") as String)

            var imageView: ImageView = findViewById(R.id.imageView)
            var calorie: TextView = findViewById(R.id.calorie)
            var btn: Button = findViewById(R.id.btn)

            picture.requestPicture(image, date, id, sex, weight, height, age).enqueue(object : Callback<Cal> {
                override fun onResponse(call: Call<Cal>, response: Response<Cal>) {
                    var login = response.body()
                    if (login?.code == "0000") {
                        Toast.makeText(applicationContext, "성공 "+ login?.msg, Toast.LENGTH_SHORT).show()
                        val imageBytes = Base64.decode(login?.img, 0)
                        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        imageView.setImageBitmap(image)
                        calorie.text = login?.msg

                        btn.setOnClickListener {
                            var outIntent = Intent(applicationContext, CalendarPage::class.java)
                            outIntent.putExtra("cal", login?.msg)
                            setResult(Activity.RESULT_OK, outIntent)
                            finish()
                        }

                    } else {
                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Cal>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신실패", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun newJpgFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }
}