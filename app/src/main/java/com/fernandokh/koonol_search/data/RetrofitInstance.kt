package com.fernandokh.koonol_search.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://koonol-api.onrender.com/api/")
        .baseUrl("http://192.168.0.5:3000/api/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}