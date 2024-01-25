package com.fetch.weather.ui.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.fragment.BaseFragment
import com.fetch.weather.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
    val viewModel: LoginViewModel by viewModel()

    override val lifecycleObserver = LoginLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun navigateToDashboard() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
    }

}