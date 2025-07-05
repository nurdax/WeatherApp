package com.tekom.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tekom.weatherapp.data.repository.WeatherRepository
import com.tekom.weatherapp.ui.citylist.CityListFragment
import com.tekom.weatherapp.ui.weather.WeatherFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val repository by lazy { WeatherRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val defaultCity = repository.getDefaultCity()
            if (defaultCity != null) {
                showWeatherFragment(defaultCity)
            } else {
                showCityListFragment()
            }
        }
    }

    fun showWeatherFragment(city: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, WeatherFragment.newInstance(city))
            .addToBackStack(null)
            .commit()
    }

    fun showCityListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CityListFragment())
            .commit()
    }
}
