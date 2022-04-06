package com.example.test_project_1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SaveFood {
    @FormUrlEncoded
    @POST("/saveFood/")
    fun saveFood(
        @Field("food_name") food_name:String,
        @Field("time") time:Int,
        @Field("id") id:String,
        @Field("sex") sex:String,
        @Field("weight") weight:Int,
        @Field("height") height:Int,
        @Field("age") age:Int
    ) : Call<Food>
}