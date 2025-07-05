package com.tekom.weatherapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekom.weatherapp.R
import com.tekom.weatherapp.data.model.Forecast

class ForecastAdapter : ListAdapter<Forecast, ForecastAdapter.ViewHolder>(ForecastDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(forecast: Forecast) {
            itemView.findViewById<TextView>(R.id.forecast_date).text = forecast.date
            itemView.findViewById<TextView>(R.id.forecast_temp).text = "${forecast.day.maxtemp_c}°C"
            itemView.findViewById<TextView>(R.id.forecast_condition).text = forecast.day.condition.text
            val iconUrl = "https:${forecast.day.condition.icon}"
            Glide.with(itemView.context)
                .load(iconUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(itemView.findViewById<ImageView>(R.id.forecast_icon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ForecastDiffCallback : DiffUtil.ItemCallback<Forecast>() {
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast) = oldItem.date == newItem.date
    override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast) = oldItem == newItem
}