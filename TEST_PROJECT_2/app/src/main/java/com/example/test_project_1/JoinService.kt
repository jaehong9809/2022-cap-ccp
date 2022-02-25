package com.example.test_project_1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface JoinService {
    @FormUrlEncoded
    @POST("/join/")
    fun requestJoin(
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("age") age:Int,
        @Field("sex") sex:String,
        @Field("weight") weight:Int
    ) : Call<Login>
}