package com.example.test_project_1

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class Loading(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var imageView: ImageView = findViewById((R.id.imageView))
        Glide.with(getContext()).load(R.raw.loading).into(imageView)
    }
}