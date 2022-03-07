package com.example.edvora.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponse(
    val name: String = "name",
    val station_code: Int = 0,
    val url: String = "url"
)