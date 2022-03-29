package com.example.test_project_1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserInfo {
    @FormUrlEncoded
    @POST("/userinfo/")
    fun search(
        @Field("id") id:String,
    ) : Call<User>
}