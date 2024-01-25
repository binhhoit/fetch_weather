
package com.fetch.weather.ui.feature.ui.welcome

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

    private val mLanguageLocaleStateObserver: Observer<Locale> = mock()

    override fun createViewModel(): WelcomeViewModel {
        return WelcomeViewModelImpl(localData)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(localData.localeCurrent).thenReturn(Locale.ENGLISH)
        super.setUp()
    }


    @Test
    fun `get language Local state`() {
        mViewModel.languageLocaleState.observeForever(mLanguageLocaleStateObserver)

        verify(mLanguageLocaleStateObserver, Times(1))
            .onChanged(Locale.ENGLISH)
    }
}