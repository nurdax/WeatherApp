package com.tekom.weatherapp.ui.citylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekom.weatherapp.R
import com.tekom.weatherapp.data.model.City

class CityListAdapter(
    var defaultCity: String?,
    private val onCityClick: (City) -> Unit,
    private val onRemoveClick: (City) -> Unit,
) : ListAdapter<City, CityListAdapter.ViewHolder>(CityDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city: City, defaultCity: String?, onCityClick: (City) -> Unit, onRemoveClick: (City) -> Unit) {
            val cityNameTextView = itemView.findViewById<TextView>(R.id.city_name)
            cityNameTextView.text = city.name
            cityNameTextView.isEnabled = !city.name.equals(defaultCity, ignoreCase = true)
            itemView.setOnClickListener { onCityClick(city) }
            itemView.findViewById<Button>(R.id.remove_button).setOnClickListener { onRemoveClick(city) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), defaultCity, onCityClick, onRemoveClick)
    }
}

class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: City, newItem: City) = oldItem == newItem
}