package com.example.sunscout

import retrofit2.http.GET
import retrofit2.http.Query


data class WeatherAPIResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val uv: Double
)

interface WeatherAPIService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String // Format as "latitude,longitude"
    ): WeatherAPIResponse
}