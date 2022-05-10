package com.example.test_project_1.join

import com.example.test_project_1.login.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IdCheck {
    @FormUrlEncoded
    @POST("/check/")
    fun idCheck(
        @Field("id") id:String,
    ) : Call<Join>
}