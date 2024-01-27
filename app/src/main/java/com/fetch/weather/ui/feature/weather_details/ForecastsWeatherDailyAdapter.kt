package com.fetch.weather.ui.feature.weather_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fetch.weather.R
import com.fetch.weather.databinding.ItemForecastsWeatherBinding
import com.fetch.weather.utils.convertTempKelvinToCelsius
import com.fetch.weather.utils.roundDecimal
import com.fetch.weather.utils.simpleFormatTime

class ForecastsWeatherDailyAdapter
    : ListAdapter<ForecastsWeatherDTO, ForecastsWeatherDailyAdapter.ViewHolder>(DIFF_CALLBACK) {

    var callback: (ForecastsWeatherDTO) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemForecastsWeatherBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemForecastsWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ForecastsWeatherDTO) = with(binding) {
            tvTimeLine.text = item.timeLine.simpleFormatTime()
            tvWind.text = item.speedWind.roundDecimal() + itemView.context.getString(R.string.fragment_weather_details_swin_symbol)
            tvTemperature.text = item.temp.convertTempKelvinToCelsius().roundDecimal() + itemView.context.getString(R.string.fragment_weather_details_symbol_celsius)
            tvHumidity.text = "${item.humidity.roundDecimal()} %"
            Glide
                .with(itemView.context)
                .load("https://openweathermap.org/img/wn/${item.weatherIcon}@2x.png")
                .placeholder(R.drawable.ic_weather_logo)
                .centerCrop()
                .into(ivCloud)
            root.setOnClickListener { callback.invoke(item) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForecastsWeatherDTO>() {
            override fun areItemsTheSame(oldItem: ForecastsWeatherDTO, newItem: ForecastsWeatherDTO): Boolean {
                return oldItem.timeLine == newItem.timeLine
            }

            override fun areContentsTheSame(oldItem: ForecastsWeatherDTO, newItem: ForecastsWeatherDTO): Boolean {
                return oldItem.timeLine == newItem.timeLine
            }
        }
    }
}