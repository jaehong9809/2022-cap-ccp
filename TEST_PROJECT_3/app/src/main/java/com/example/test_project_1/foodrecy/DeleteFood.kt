package com.example.test_project_1.foodrecy

import com.example.test_project_1.Food
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DeleteFood {
    @FormUrlEncoded
    @POST("/delete/")
    fun deleteFood(
        @Field("time") time:String,
        @Field("id") id:String,
        @Field("food") food:String
    ) : Call<Food>
}