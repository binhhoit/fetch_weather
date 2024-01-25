package com.fetch.weather.ui.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.activity.BaseActivity
import com.base.fragment.BaseFragment
import com.data.exception.UnAuthException
import com.data.model.DataState
import com.fetch.weather.R
import com.fetch.weather.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LoginFragment : BaseFragment() {

    companion object{
        const val BOTTOM_SHEET_DATE_PICKER = "bottom_sheet_date_picker"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override val lifecycleObserver = LoginLifecycleObserver(this)

    private val viewModel: LoginViewModel by viewModel()


    private var selectedDay: Int? = null
    private var selectedMonth: Int? = null
    private var selectedYear: Int? = null

    override val requestStatusBar = DARK

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            btnLogin.setOnClickListener {
//                onLogin()
            }

            tvCreateAccount.setOnClickListener {

            }

            ivCloseMsg.setOnClickListener {
                showIncorrectPassword = false
            }
        }

        val now = Calendar.getInstance()
        selectedYear = now.get(Calendar.YEAR)
        selectedMonth = now.get(Calendar.MONTH) + 1
        selectedDay = now.get(Calendar.DAY_OF_MONTH)

    }

    private fun initViewModel() {
        viewModel.apply {
            loginLiveData.observe(this@LoginFragment.viewLifecycleOwner) { state ->
                when (state) {
                    is DataState.Loading -> {
                        binding.apply {
                            if (state.isLoading) {
                                showIncorrectPassword = false
                                btnLogin.setText(R.string.signing_in_in_loading)
                                btnLogin.isEnabled = false
                                btnLogin.alpha = 0.5f
                            } else {
                                btnLogin.setText(R.string.sign_in)
                                btnLogin.isEnabled = true
                                btnLogin.alpha = 1f
                            }
                        }
                    }

                    is DataState.Success<*> -> {
                        (requireActivity() as BaseActivity).getInformDialog()
                            .show(description = getString(R.string.login_success))
                    }

                    is DataState.Failure -> {
                        val throwable = state.message
                        if (throwable is UnAuthException) {
                            binding.showIncorrectPassword = true
                        } else {
                            showError(throwable)
                        }
                    }
                }
            }
        }
    }

    private fun validateLoginField(): Boolean {
        var canLogin = true
        viewModel.apply {
            if (binding.edtEmail.text.toString().isEmpty()) {
                binding.tilEmail.isErrorEnabled = true
                binding.tilEmail.error = getString(R.string.required_field)
                canLogin = false
            }
            if (binding.edtPassword.text.toString().isEmpty()) {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = getString(R.string.required_field)
                canLogin = false
            }
        }
        return canLogin
    }

    private fun resetInputFieldState() {
        binding.apply {
            tilEmail.isErrorEnabled = false
            tilEmail.error = null
            tilPassword.isErrorEnabled = false
            tilPassword.error = null
        }
    }

    private fun onLogin() {
        resetInputFieldState()
        if (validateLoginField()) {
            viewModel.doLogin(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
        }
    }


    private fun updateTime() {
        val cal = Calendar.getInstance()
        cal.set(selectedYear!!, selectedMonth!! - 1, selectedDay!!)
        binding.tvDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(cal.time)
    }
}