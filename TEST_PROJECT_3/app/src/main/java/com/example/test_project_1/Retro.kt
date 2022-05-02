package com.example.test_project_1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro{
    var retrofit = Retrofit.Builder()
        .baseUrl("http://:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}