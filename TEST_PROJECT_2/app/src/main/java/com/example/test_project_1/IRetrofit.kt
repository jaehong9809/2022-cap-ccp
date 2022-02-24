package com.example.test_project_1
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    @FormUrlEncoded
    @POST("/user/login")
    fun loginRequest(
        @Field("userID") userid: String,
        @Field("userPwd") password: String,
        @Field("userName") username:String
    ): Call<JsonElement>
}