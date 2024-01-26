package com.fetch.weather.ui.feature.weather_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.base.fragment.BaseFragment
import com.fetch.weather.databinding.FragmentWeatherDetailBinding
import com.fetch.weather.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherDetailFragment : BaseFragment() {
    private var _binding: FragmentWeatherDetailBinding? = null
    val binding get() = _binding!!
    val viewModel: WeatherDetailViewModel by viewModel()
    val argument: WeatherDetailFragmentArgs by navArgs()

    override val lifecycleObserver = WeatherDetailLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}