package com.example.edvora.models

data class RideResponse(
    val city: String,
    val date: String,
    val destination_station_code: Int,
    val id: Int,
    val map_url: String,
    val origin_station_code: Int,
    val state: String,
    val station_path: ArrayList<Int>,
    var distance:Int=-1
)