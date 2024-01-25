package com.fetch.weather.ui.feature.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.fragment.BaseFragment
import com.fetch.weather.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: WelcomeViewModel by viewModel()

    val pbLoading by lazy { binding.pbLoading }

    override val lifecycleObserver = WelcomeLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun navigateToLoginScreen(){
        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
    }

    fun navigateToDashboardScreen(){
        findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToDashboardFragment())
    }

}