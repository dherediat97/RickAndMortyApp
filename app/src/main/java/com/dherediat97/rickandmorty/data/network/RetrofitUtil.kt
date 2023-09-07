package com.dherediat97.rickandmorty.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val rickMortyApiService: RickMortyApiService by lazy {
        retrofit.create(RickMortyApiService::class.java)
    }
}