package com.fetch.weather.ui.feature.dashboard.profile

import androidx.lifecycle.Observer
import com.data.domain.user.ProfileUseCase
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest: BaseViewModelTest<ProfileViewModel>() {

    @Mock
    private lateinit var profileUseCase: ProfileUseCase
    private val mDataStateObserver: Observer<DataState<String>> = mock()

    override fun createViewModel(): ProfileViewModel {
        return ProfileViewModelImpl(profileUseCase)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        super.setUp()
    }
    
    @Test
    fun `get profile success, test DataState Success`() = testCoroutineRule.runTest {
        // arrange
        whenever(profileUseCase.getProfileInfo()).thenReturn(flowOf("name"))
        mViewModel.profileState.observeForever(mDataStateObserver)

        // action
        mViewModel.getProfile()

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Success<String>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }

    @Test
    fun `get profile fail, test DataState Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(profileUseCase.getProfileInfo())
            .thenReturn(flow { throw Throwable("API error") })
        mViewModel.profileState.observeForever(mDataStateObserver)

        // action
        mViewModel.getProfile()

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Failure<String>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }

    @Test
    fun `logout success`() = testCoroutineRule.runTest{
        // arrange
        whenever(profileUseCase.logOutProfile())
            .thenReturn(flowOf(true))
        mViewModel.profileState.observeForever(mDataStateObserver)

        // action
        mViewModel.logOut()

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }
    }

    @Test
    fun `logout Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(profileUseCase.logOutProfile())
            .thenReturn(flow { throw Throwable("API error") })
        mViewModel.profileState.observeForever(mDataStateObserver)

        // action
        mViewModel.logOut()


        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }
    }
}