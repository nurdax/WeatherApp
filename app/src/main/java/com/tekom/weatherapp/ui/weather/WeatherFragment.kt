package com.tekom.weatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tekom.weatherapp.MainActivity
import com.tekom.weatherapp.data.repository.WeatherRepository
import com.tekom.weatherapp.databinding.FragmentWeatherBinding
import com.tekom.weatherapp.ui.main.ForecastAdapter

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding

    companion object {
        fun newInstance(city: String) = WeatherFragment().apply {
            arguments = Bundle().apply { putString("city", city) }
        }
    }

    private val viewModel: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WeatherViewModel(
                    arguments?.getString("city")!!,
                    WeatherRepository(requireContext())
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        val recyclerView = binding.forecastList
        val swipeRefresh = binding.swipeRefresh
        val adapter = ForecastAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            binding.cityName.text = weather.location.name
            binding.temperature.text = "${weather.current.temp_c}°C"
            binding.condition.text = weather.current.condition.text
            binding.wind.text = "Ветер: ${weather.current.wind_kph} км/час"
            binding.humidity.text = "Влажность: ${weather.current.humidity}%"
            val iconUrl = "https:${weather.current.condition.icon}"
            Glide.with(this)
                .load(iconUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(binding.weatherIcon)
            adapter.submitList(weather.forecast.forecastday)
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            swipeRefresh.isRefreshing = isRefreshing
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.refreshWeather()
        }

        binding.setDefaultButton.setOnClickListener {
            viewModel.setDefaultCity()
            Toast.makeText(context, "${binding.cityName.text} - по умолчанию", Toast.LENGTH_SHORT).show()
        }

        binding.changeCityButton.setOnClickListener {
            (activity as MainActivity).showCityListFragment()
        }

        return binding.root
    }
}