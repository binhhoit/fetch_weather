package com.fetch.weather.ui.feature.weather_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.data.model.WeatherDetail
import com.fetch.weather.R
import com.fetch.weather.databinding.ItemForecastsWeatherBinding
import kotlin.math.roundToLong

class ForecastsWeatherDailyAdapter : ListAdapter<WeatherDetail, ForecastsWeatherDailyAdapter.ViewHolder>(DIFF_CALLBACK) {

    var callback: (WeatherDetail) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemForecastsWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemForecastsWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherDetail) = with(binding) {
            tvTimeLine.text = item.timeLine
            tvWind.text = item.wind?.speed.toString() + " m/s N"
            tvTemperature.text = ((item.main?.temp?:0.0) - 273).roundToLong().toString()+ "°C"
            tvHumidity.text = item.main?.humidity.toString() + "%"
            Glide
                .with(itemView.context)
                .load("https://openweathermap.org/img/wn/${item.weather.first().icon?.replace("n","d")}@2x.png")
                .placeholder(R.drawable.ic_weather_logo)
                .centerCrop()
                .into(ivCloud)
            root.setOnClickListener { callback.invoke(item) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WeatherDetail>() {
            override fun areItemsTheSame(oldItem: WeatherDetail, newItem: WeatherDetail): Boolean {
                return oldItem.timeLine == newItem.timeLine
            }

            override fun areContentsTheSame(oldItem: WeatherDetail, newItem: WeatherDetail): Boolean {
                return oldItem.timeLine == newItem.timeLine
            }
        }
    }
}