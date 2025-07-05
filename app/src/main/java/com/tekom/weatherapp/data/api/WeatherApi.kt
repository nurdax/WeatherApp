package com.tekom.weatherapp.data.api

import com.tekom.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: Int = 5,
        @Query("lang") lang: String = "ru"
    ): WeatherResponse
}