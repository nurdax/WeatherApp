package com.tekom.weatherapp.ui.citylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekom.weatherapp.data.model.City
import com.tekom.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class CityListViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val _defaultCity = MutableLiveData<String?>()
    val defaultCity: LiveData<String?> = _defaultCity

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadCities()
        loadDefaultCity()
    }

    fun refreshCities() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadCities()
            loadDefaultCity()
            _isRefreshing.value = false
        }
    }

    fun addCity(cityName: String) {
        viewModelScope.launch {
            try {
                repository.addCity(cityName)
                loadCities()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось добавить город: ${e.message}"
            }
        }
    }

    fun removeCity(cityName: String) {
        viewModelScope.launch {
            try {
                repository.removeCity(cityName)
                loadCities()
                loadDefaultCity()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось удалить город: ${e.message}"
            }
        }
    }


    private fun loadCities() {
        viewModelScope.launch {
            try {
                _cities.value = repository.getCities()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить города: ${e.message}"
            }
        }
    }

    private fun loadDefaultCity() {
        viewModelScope.launch {
            try {
                _defaultCity.value = repository.getDefaultCity()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Не удалось загрузить город по умолчанию: ${e.message}"
            }
        }
    }
}