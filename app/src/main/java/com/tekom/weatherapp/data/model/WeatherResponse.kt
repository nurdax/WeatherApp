package com.tekom.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val city: String,
    val temperature: Float,
    val conditionText: String,
    val conditionIcon: String,
    val windKph: Float,
    val humidity: Int,
    val forecasts: List<Forecast>,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather,
    val forecast: ForecastData
)

data class Location(val name: String)
data class CurrentWeather(
    val temp_c: Float,
    val condition: Condition,
    val wind_kph: Float,
    val humidity: Int
)
data class Condition(val text: String, val icon: String)
data class ForecastData(val forecastday: List<Forecast>)
data class Forecast(
    val date: String,
    val day: Day
)
data class Day(val maxtemp_c: Float, val condition: Condition)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromForecastList(forecasts: List<Forecast>): String {
        return gson.toJson(forecasts)
    }

    @TypeConverter
    fun toForecastList(json: String): List<Forecast> {
        val type = object : TypeToken<List<Forecast>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}