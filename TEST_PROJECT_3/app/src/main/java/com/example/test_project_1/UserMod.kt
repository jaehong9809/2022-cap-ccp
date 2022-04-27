package com.example.test_project_1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserMod {
    @FormUrlEncoded
    @POST("/usermod/")
    fun mod(
        @Field("id") id:String,
        @Field("sex") sex:String,
        @Field("height") height:Int,
        @Field("weight") weight:Int,
        @Field("age") age:Int
    ) : Call<User>
}