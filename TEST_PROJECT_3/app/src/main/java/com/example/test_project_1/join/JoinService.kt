package com.example.test_project_1.join

import com.example.test_project_1.login.Login
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface JoinService {
    @FormUrlEncoded
    @POST("/join/")
    fun requestJoin(
        @Field("id") id:String,
        @Field("password") password:String,
        @Field("age") age:Int,
        @Field("sex") sex:String,
        @Field("height") height:Int,
        @Field("weight") weight:Int
    ) : Call<Join>
}