package com.atar.galeria.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GaleriaBridge {

    private const val BASE_URL = "https://api.gurushots.com/rest_mobile/"

    val instance: GaleriaApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GaleriaApi::class.java)
    }

}