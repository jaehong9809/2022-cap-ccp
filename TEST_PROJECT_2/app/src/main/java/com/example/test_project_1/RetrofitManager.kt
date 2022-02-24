package com.example.test_project_1

import android.util.Log
import com.example.test_project_1.Loginresponse
import com.example.test_project_1.Retrofitclient
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitManager {
    private val BASE_URL = "13.209.69.90:3000"
    private val TAG: String = "로그"

    companion object{
        val instance = RetrofitManager()
    }

    private val iRetrofit: IRetrofit? = Retrofitclient.getClient(BASE_URL)?.create(IRetrofit::class.java)

    fun login(userID: String, password: String, username:String, completion: (Loginresponse, String) -> Unit){
        var call = iRetrofit?.loginRequest(userID, password, username) ?: return

        call.enqueue(object: Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure: ");
                completion(Loginresponse.FAIL, t.toString())
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse: ${response.body()} ");
                Log.d(TAG, "RetrofitManager - onResponse: status code is ${response.code()}");
                if(response.code() != 200){
                    completion(Loginresponse.FAIL, response.body().toString())
                }else{
                    completion(Loginresponse.OK, response.body().toString())
                }
            }
        })
    }
}