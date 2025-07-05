package com.tekom.weatherapp.data.cache

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tekom.weatherapp.data.model.City
import com.tekom.weatherapp.data.model.WeatherEntity
import com.tekom.weatherapp.data.model.WeatherResponse
import com.tekom.weatherapp.data.model.Location
import com.tekom.weatherapp.data.model.CurrentWeather
import com.tekom.weatherapp.data.model.Condition
import com.tekom.weatherapp.data.model.ForecastData

class WeatherCache(private val context: Context) {
    private val database = WeatherDatabase.getDatabase(context)
    private val cityDao = database.cityDao()
    private val weatherDao = database.weatherDao()

    suspend fun saveWeather(city: String, weather: WeatherResponse) {
        val weatherEntity = WeatherEntity(
            city = city,
            temperature = weather.current.temp_c,
            conditionText = weather.current.condition.text,
            conditionIcon = weather.current.condition.icon,
            windKph = weather.current.wind_kph,
            humidity = weather.current.humidity,
            forecasts = weather.forecast.forecastday
        )
        weatherDao.insertWeather(weatherEntity)
    }

    suspend fun getWeather(city: String): WeatherResponse? {
        val weatherEntity = weatherDao.getWeather(city) ?: return null
        return WeatherResponse(
            location = Location(name = city),
            current = CurrentWeather(
                temp_c = weatherEntity.temperature,
                condition = Condition(
                    text = weatherEntity.conditionText,
                    icon = weatherEntity.conditionIcon
                ),
                wind_kph = weatherEntity.windKph,
                humidity = weatherEntity.humidity
            ),
            forecast = ForecastData(forecastday = weatherEntity.forecasts)
        )
    }

    suspend fun saveDefaultCity(city: String) {
        val prefs = context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
        prefs.edit().putString("default_city", city).apply()
    }

    suspend fun getDefaultCity(): String? {
        val prefs = context.getSharedPreferences("weather_cache", Context.MODE_PRIVATE)
        return prefs.getString("default_city", null)
    }

    suspend fun saveCities(cities: List<City>) {
        cities.forEach { cityDao.insertCity(it) }
    }

    suspend fun getCities(): List<City> {
        val cities = cityDao.getAllCities()
        return if (cities.isNotEmpty()) {
            cities
        } else {
            val defaultCities = listOf(
                City("Москва"), City("Нижний Новгород"), City("Владивосток"),
                City("Казань"), City("Санкт-Петербург")
            )
            saveCities(defaultCities)
            defaultCities
        }
    }

    suspend fun removeCity(cityName: String) {
        cityDao.deleteCity(cityName)
    }
}