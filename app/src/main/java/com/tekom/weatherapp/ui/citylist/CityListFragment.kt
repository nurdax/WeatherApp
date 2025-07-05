package com.tekom.weatherapp.ui.citylist

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
import com.tekom.weatherapp.MainActivity
import com.tekom.weatherapp.data.repository.WeatherRepository
import com.tekom.weatherapp.databinding.FragmentCityListBinding

class CityListFragment : Fragment() {
    private lateinit var binding: FragmentCityListBinding

    private val viewModel: CityListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CityListViewModel(WeatherRepository(requireContext())) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityListBinding.inflate(inflater, container, false)

        val recyclerView = binding.cityList
        val swipeRefresh = binding.swipeRefresh
        val addCityEditText = binding.addCityInput
        val addCityButton = binding.addCityButton

        val adapter = CityListAdapter(
            defaultCity = null,
            onCityClick = { city -> (activity as MainActivity).showWeatherFragment(city.name) },
            onRemoveClick = { city ->
                viewModel.removeCity(city.name)
                Toast.makeText(context, "${city.name} удалено", Toast.LENGTH_SHORT).show()
            }

        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            adapter.submitList(cities)
        }

        viewModel.defaultCity.observe(viewLifecycleOwner) { defaultCity ->
            adapter.defaultCity = defaultCity
            adapter.notifyDataSetChanged()
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
            viewModel.refreshCities()
        }

        addCityButton.setOnClickListener {
            val cityName = addCityEditText.text.toString().trim()
            if (cityName.isNotEmpty()) {
                viewModel.addCity(cityName)
                addCityEditText.text.clear()
                Toast.makeText(context, "$cityName добавлена", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Напиши название города", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}