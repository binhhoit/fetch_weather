
package com.fetch.weather.ui.feature.permission.file

import androidx.lifecycle.Observer
import com.data.common.SharePreferenceManager
import com.fetch.weather.viewmodel.BaseViewModelTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

class PermissionFileAccessViewModelTest : BaseViewModelTest<PermissionFileAccessViewModel>() {

    @Mock
    private lateinit var localData: SharePreferenceManager

    private val stateObserver: Observer<String> = mock()

    override fun createViewModel(): PermissionFileAccessViewModel {
        return PermissionFileAccessViewModelImpl(localData)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(localData.uriAccessFolder).thenReturn("")
        super.setUp()
    }

    @Test
    fun `save uri file access`() {
        mViewModel.saveUrlFolderAccess("uri_file_save")

        mViewModel.uriFolderAccessData.observeForever(stateObserver)

        verify(stateObserver, Times(1))
            .onChanged(value = "uri_file_save")
    }

    @Test
    fun `get uri file access`() {
        mViewModel.uriFolderAccessData.observeForever(stateObserver)

        verify(stateObserver, Times(1))
            .onChanged(value = "")
    }
}