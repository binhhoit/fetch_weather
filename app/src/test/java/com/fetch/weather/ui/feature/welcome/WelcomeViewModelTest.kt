
package com.fetch.weather.ui.feature.welcome

import androidx.lifecycle.Observer
import com.data.common.SharePreferenceManager
import com.fetch.weather.ui.feature.welcome.WelcomeViewModel
import com.fetch.weather.ui.feature.welcome.WelcomeViewModelImpl
import com.fetch.weather.viewmodel.BaseViewModelTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import java.util.Locale

class WelcomeViewModelTest : BaseViewModelTest<WelcomeViewModel>() {

    @Mock
    private lateinit var localData: SharePreferenceManager

    private val mFirstOpenAppStateObserver: Observer<Boolean> = mock()

    override fun createViewModel(): WelcomeViewModel {
        return WelcomeViewModelImpl(localData)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `check first open app state`() {
        whenever(localData.userToken).thenReturn("")
        whenever(localData.userNameInfo).thenReturn("")
        super.setUp()
        mViewModel.isLogin.observeForever(mFirstOpenAppStateObserver)

        verify(mFirstOpenAppStateObserver, Times(1))
            .onChanged(false)
    }

    @Test
    fun `check second open app state`() {
        whenever(localData.userToken).thenReturn("token")
        whenever(localData.userNameInfo).thenReturn("user_name")
        super.setUp()
        mViewModel.isLogin.observeForever(mFirstOpenAppStateObserver)

        verify(mFirstOpenAppStateObserver, Times(1))
            .onChanged(true)
    }
}