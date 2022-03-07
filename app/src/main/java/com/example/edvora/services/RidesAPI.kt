package com.example.edvora.services

import com.example.edvora.models.RidesResponse
import com.example.edvora.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface RidesAPI {

    @GET("rides")
    suspend fun getRides():Response<RidesResponse>

    @GET("user")
    suspend fun getUser():Response<UserResponse>

}