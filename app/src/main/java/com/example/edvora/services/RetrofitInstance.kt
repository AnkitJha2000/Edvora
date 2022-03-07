package com.example.edvora.services

import com.example.edvora.models.RidesItemDeserializer
import com.example.edvora.models.RidesResponse
import com.example.edvora.utils.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// this will create the instance of retrofit explicitly

class RetrofitInstance {

    companion object{

        private val retrofit by lazy{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            val gson_gd = GsonBuilder().registerTypeAdapter(
                RidesResponse::class.java,
                RidesItemDeserializer()
            ).create()

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson_gd)).client(client).build()

        }

        val api by lazy{
            retrofit.create(RidesAPI::class.java)
        }

    }

}