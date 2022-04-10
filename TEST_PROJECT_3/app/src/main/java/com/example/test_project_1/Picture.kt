package com.example.test_project_1

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Picture {
    @Multipart
    @POST("/calculate/")
    fun requestPicture(
        @Part image : MultipartBody.Part,
    ) : Call<Cal>
}