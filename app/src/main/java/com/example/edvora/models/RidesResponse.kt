package com.example.edvora.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RidesResponse(
    //@JsonProperty("ride")
    val ride: List <RideResponse>
)