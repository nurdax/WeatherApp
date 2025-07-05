package com.tekom.weatherapp.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tekom.weatherapp.data.model.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE city = :city")
    suspend fun getWeather(city: String): WeatherEntity?

    @Query("SELECT * FROM weather WHERE city = :city")
    suspend fun getDefaultCityWeather(city: String): WeatherEntity?
}