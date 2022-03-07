package com.example.edvora.repository

import com.example.edvora.services.RetrofitInstance
import com.example.edvora.services.RidesAPI

class RidesRepository () {

    suspend fun getRides() = RetrofitInstance.api.getRides()

    suspend fun getUser() = RetrofitInstance.api.getUser()

}