package com.example.test_project_1

import com.example.test_project_1.Food
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FoodService {
    @FormUrlEncoded
    @POST("/food/")
    fun searchFood(
        @Field("time") time:String,
        @Field("id") id:String
    ) : Call<Food>
}