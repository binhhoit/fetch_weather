package com.fetch.weather.ui.feature.dashboard.favorite

import androidx.lifecycle.Observer
import com.data.domain.location.GeocodingFavoriteUseCase
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
class FavoriteViewModelTest: BaseViewModelTest<FavoriteViewModel>() {

    @Mock
    private lateinit var geocodingFavoriteUseCase: GeocodingFavoriteUseCase
    private val mDataStateObserver: Observer<DataState<List<LocationResponse>>> = mock()

    override fun createViewModel(): FavoriteViewModel {
        return FavoriteViewModelImpl(geocodingFavoriteUseCase)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        super.setUp()
    }
    
    @Test
    fun `get list location favorite success, test DataState Success`() = testCoroutineRule.runTest {
        // arrange
        val locations = listOf(LocationResponse(name = "HCM"))
        whenever(geocodingFavoriteUseCase.execute(any())).thenReturn(flowOf(locations))
        mViewModel.geocodingState.observeForever(mDataStateObserver)

        // action
        mViewModel.getListLocationFavorite()

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Success<List<LocationResponse>>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }

    @Test
    fun `get list location favorite fail, test DataState Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(geocodingFavoriteUseCase.execute(any()))
            .thenReturn(flow { throw Throwable("API error") })
        mViewModel.geocodingState.observeForever(mDataStateObserver)

        // action
        mViewModel.getListLocationFavorite()

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(true))
        verify(mDataStateObserver, Times(1)).onChanged(any<DataState.Failure<List<LocationResponse>>>())
        verify(mDataStateObserver, Times(1)).onChanged(DataState.Loading(false))
    }
}