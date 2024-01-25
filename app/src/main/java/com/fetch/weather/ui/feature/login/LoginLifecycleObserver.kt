package com.fetch.weather.ui.feature.login

import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.data.model.DataState
import com.fetch.weather.R

class LoginLifecycleObserver(private val fragment: LoginFragment) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        validInputName()
        actionButtonLogin()
        observeData()
    }

    private fun validInputName() {
        fragment.apply {
            binding.apply {
                edtDisplayName.addTextChangedListener { uName ->
                    btnLogin.isEnabled = false
                    if (TextUtils.isEmpty(uName)) {
                        tvError.isVisible = true
                        tvError.text =
                            getString(R.string.fragment_login_valid_name_empty)
                    } else if (uName!!.length <= 4) {
                        tvError.isVisible = true
                        tvError.text =
                            getString(R.string.fragment_login_valid_name_short)
                    } else {
                        tvError.isVisible = false
                        btnLogin.isEnabled = true
                    }
                }
            }
        }
    }

    private fun actionButtonLogin() {
        fragment.binding.apply {
            btnLogin.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        fragment.apply {
            binding.apply {
                viewModel.doLogin(edtDisplayName.text ?: "")
            }
        }
    }

    private fun observeData() {
        fragment.apply {
            viewModel.loginLiveData.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is DataState.Loading -> {
                        if (state.isLoading) showLoading() else hideLoading()
                    }

                    is DataState.Success -> {
                       navigateToDashboard()
                    }

                    is DataState.Failure -> {
                       showError(Throwable(getString(R.string.fragment_login_can_t_login)))
                    }
                }
            }
        }
    }


}
