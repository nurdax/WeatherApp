package com.tekom.weatherapp.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekom.weatherapp.data.model.WeatherResponse
import com.tekom.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val city: String,
    private val repository: WeatherRepository
) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadWeather()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadWeather()
            _isRefreshing.value = false
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            try {
                _weather.value = repository.getWeather(city)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить данные о погоде: ${e.message}"
                _weather.value = repository.getWeather(city)
            }
        }
    }

    fun setDefaultCity() {
        viewModelScope.launch {
            try {
                repository.saveDefaultCity(city)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось установить город по умолчаниюy: ${e.message}"
            }
        }
    }
}