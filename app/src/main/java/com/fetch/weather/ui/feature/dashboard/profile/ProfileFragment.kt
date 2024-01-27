package com.fetch.weather.ui.feature.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.fragment.BaseFragment
import com.fetch.weather.databinding.FragmentProfileBinding
import com.fetch.weather.ui.feature.dashboard.DashboardFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {
    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!
    val viewModel: ProfileViewModel by viewModel()

    override val lifecycleObserver = ProfileLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override val requestStatusBar = LIGHT

    fun navigateToLoginPage() {
        findNavControllerDashboard()?.navigate(
            DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
        )
    }

    private fun findNavControllerDashboard() =
        parentFragment?.parentFragment?.findNavController()
}