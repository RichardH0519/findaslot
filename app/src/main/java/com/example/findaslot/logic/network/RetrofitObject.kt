package com.example.findaslot.logic.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private var BASE_URL = "https://maps.googleapis.com/maps/api/"
    val retrofitService: RetrofitInterface by lazy {
        Log.i("RetrofitObject", "called")
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(RetrofitInterface::class.java)
    }
}