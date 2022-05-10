package com.example.test_project_1.login

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface LoginService {

    @FormUrlEncoded
    @POST("/login/")
    fun requestLogin(
        @Field("id") id:String,
        @Field("password") password:String
    ) : Call<Login>
}