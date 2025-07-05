package com.tekom.weatherapp.data.repository

import android.content.Context
import com.tekom.weatherapp.data.api.WeatherApiService
import com.tekom.weatherapp.data.cache.WeatherCache
import com.tekom.weatherapp.data.model.City
import com.tekom.weatherapp.data.model.WeatherResponse

class WeatherRepository(context: Context) {
    private val api = WeatherApiService.api
    private val cache = WeatherCache(context)
    private val apiKey = "760ea5d5862d4cc0b8b62312250207"


    suspend fun getWeather(city: String): WeatherResponse {
        return try {
            val response = api.getWeather(apiKey, city, lang = "ru")
            cache.saveWeather(city, response)
            response
        } catch (e: Exception) {
            cache.getWeather(city) ?: throw e
        }
    }

    suspend fun getDefaultCity(): String? = cache.getDefaultCity()

    suspend fun saveDefaultCity(city: String) = cache.saveDefaultCity(city)

    suspend fun getCities(): List<City> = cache.getCities()

    suspend fun addCity(cityName: String) {
        try {
            api.getWeather(apiKey, cityName)
            val currentCities = cache.getCities()
            if (currentCities.none { it.name.equals(cityName, ignoreCase = true) }) {
                cache.saveCities(listOf(City(cityName)) + currentCities)
            }
        } catch (e: Exception) {
            throw Exception("Неверное название города")
        }
    }

    suspend fun removeCity(cityName: String) {
        cache.removeCity(cityName)
    }
}