package com.fetch.weather.ui.feature.dashboard.search

import androidx.lifecycle.Observer
import com.data.domain.location.GeocodingUseCase
import com.data.model.DataState
import com.data.model.LocationResponse
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
class SearchViewModelTest: BaseViewModelTest<SearchViewModel>() {

    @Mock
    private lateinit var geocodingUseCase: GeocodingUseCase
    private val mDataStateObserver: Observer<DataState<List<LocationResponse>>> = mock()

    override fun createViewModel(): SearchViewModel {
        return SearchViewModelImpl(geocodingUseCase)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        super.setUp()
    }
    
    @Test
    fun `do search location with any key success, test DataState Success`() = testCoroutineRule.runTest {
        // arrange
        val locations = listOf(LocationResponse(name = "HCM"))
        whenever(geocodingUseCase.execute(any())).thenReturn(flowOf(locations))
        mViewModel.geocodingState.observeForever(mDataStateObserver)

        // action
        mViewModel.searchLocationName("HCM")

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Success<List<LocationResponse>>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }

    @Test
    fun `do search location with any key fail, test DataState Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(geocodingUseCase.execute(any()))
            .thenReturn(flow { throw Throwable("API error") })
        mViewModel.geocodingState.observeForever(mDataStateObserver)

        // action
        mViewModel.searchLocationName("HCM")

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Failure<List<LocationResponse>>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }
}