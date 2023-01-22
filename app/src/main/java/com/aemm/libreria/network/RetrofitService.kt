package com.aemm.libreria.network

import com.aemm.libreria.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService{

    private var INSTANCE: Retrofit? = null

    fun getRetrofit(): Retrofit {
        return INSTANCE ?: synchronized(this) {
            val instance = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            INSTANCE = instance

            instance
        }
    }
}