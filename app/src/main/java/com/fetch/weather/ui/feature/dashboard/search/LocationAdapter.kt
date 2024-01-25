package com.fetch.weather.ui.feature.dashboard.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.data.model.LocationResponse
import com.fetch.weather.databinding.ItemGeocodingBinding

class LocationAdapter : ListAdapter<LocationResponse, LocationAdapter.ViewHolder>(DIFF_CALLBACK) {

    var callback: (LocationResponse) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGeocodingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemGeocodingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocationResponse) = with(binding) {
            tvCountry.text = item.country
            tvLocalNames.text = item.localNames?.en
            root.setOnClickListener { callback.invoke(item) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationResponse>() {
            override fun areItemsTheSame(oldItem: LocationResponse, newItem: LocationResponse): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: LocationResponse, newItem: LocationResponse): Boolean {
                return oldItem.country == newItem.country &&
                        oldItem.lat == newItem.lat &&
                        oldItem.lon == newItem.lon
            }
        }
    }
}