package com.example.bookandroid.services

import com.example.bookandroid.models.TransactionItemModel
import com.example.bookandroid.models.UserModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "http://10.0.2.2:5000/"

    var token: String = ""
    var user: UserModel? = null

    var cart: ArrayList<TransactionItemModel> = arrayListOf()

    val service: RetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl("${BASE_URL}api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}