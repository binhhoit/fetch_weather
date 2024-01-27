package com.fetch.weather.ui.feature.login

import androidx.lifecycle.Observer
import com.data.domain.user.LoginUseCase
import com.data.model.DataState
import com.fetch.weather.viewmodel.BaseViewModelTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest: BaseViewModelTest<LoginViewModel>() {

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    private val mLoginDataStateObserver: Observer<DataState<Boolean>> = mock()

    override fun createViewModel(): LoginViewModel {
        return LoginViewModelImpl(loginUseCase)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        super.setUp()
    }


    @Test
    fun `do basic login success, test DataState Success`() = testCoroutineRule.runTest {
        // arrange
        whenever(loginUseCase.execute(any())).thenReturn(flowOf(true))
        mViewModel.loginLiveData.observeForever(mLoginDataStateObserver)

        // action
        mViewModel.doLogin("Testing")

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mLoginDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mLoginDataStateObserver, Times(1)).onChanged(any<DataState.Success<Boolean>>())
        verify(mLoginDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }

    @Test
    fun `do basic login fail, test DataState Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(loginUseCase.execute(any()))
            .thenReturn(flow { throw Throwable("Login error") })
        mViewModel.loginLiveData.observeForever(mLoginDataStateObserver)

        // action
        mViewModel.doLogin("")

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mLoginDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mLoginDataStateObserver, Times(1)).onChanged(any<DataState.Failure<Boolean>>())
        verify(mLoginDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }
}