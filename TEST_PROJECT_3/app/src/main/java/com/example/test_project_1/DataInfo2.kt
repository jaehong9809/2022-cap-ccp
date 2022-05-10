package com.example.test_project_1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DataInfo2 {
    @FormUrlEncoded
    @POST("/datainfo2/")
    fun search(
        @Field("sex") sex:String,
        @Field("height") height:Int,
        @Field("weight") weight:Int,
        @Field("age") age:Int,
    ) : Call<Data2>
}