package com.example.edvora.models

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.StrictMath.abs
import java.lang.reflect.Type


class RidesItemDeserializer : JsonDeserializer<RidesResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RidesResponse {

        val rideResponseList = ArrayList<RideResponse>()

        val elements: JsonArray = json!!.asJsonArray

        for(obj in elements){
            val ride = obj.asJsonObject

            val city = ride["city"].asString
            val state = ride["state"].asString
            val map_url = ride["map_url"].asString
            val date = ride["date"].asString
            val destination_station_code = ride["destination_station_code"].asInt
            val origin_station_code = ride["origin_station_code"].asInt
            val id = ride["id"].asInt
            val station_path_json = ride["station_path"]
            val station_path = ArrayList<Int>()

            for(i in station_path_json.asJsonArray)
            {
                station_path.add(i.asInt)
            }
            rideResponseList.add(RideResponse(city,date,destination_station_code,id,map_url,origin_station_code,state,station_path,-1))
        }

        return RidesResponse(rideResponseList)
    }
}