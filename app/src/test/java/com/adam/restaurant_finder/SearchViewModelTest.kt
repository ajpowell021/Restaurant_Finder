package com.adam.restaurant_finder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adam.restaurant_finder.api.GetDetails
import com.adam.restaurant_finder.api.NearbyRestaurants
import com.adam.restaurant_finder.api.SearchRestaurants
import com.adam.restaurant_finder.model.Place
import com.adam.restaurant_finder.viewModel.SearchViewModel
import io.reactivex.Flowable
import io.reactivex.Scheduler
import org.junit.*
import org.mockito.Mockito.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.plugins.RxAndroidPlugins
import junit.framework.Assert.assertEquals
import java.util.concurrent.Callable


class SearchViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val nearbyRestaurants = mock(NearbyRestaurants::class.java)
    private val searchRestaurants = mock(SearchRestaurants::class.java)
    private val getDetails = mock(GetDetails::class.java)

    private lateinit var viewModel: SearchViewModel

    private val mockPlaceId = "place_id"
    private val mockLocation = "0,0"
    private val mockPlace = Place(null, mockPlaceId, null, null, null, null, null, null, null, null)
    private val mockInput = "Pizza"

    private val mockPlaceResponse = Flowable.just(NearbyRestaurants.Response(mutableListOf(mockPlace)))
    private val mockSearchResponse = Flowable.just(SearchRestaurants.Response(mutableListOf(mockPlace)))
    private val mockDetailsResponse = Flowable.just(GetDetails.Response(mockPlace))


    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler?>? -> Schedulers.trampoline() }

        `when`(nearbyRestaurants.execute(mockLocation)).thenReturn(mockPlaceResponse)
        `when`(searchRestaurants.execute(mockInput, mockLocation)).thenReturn(mockSearchResponse)
        `when`(getDetails.execute(mockPlaceId)).thenReturn(mockDetailsResponse)

        viewModel = SearchViewModel(nearbyRestaurants, searchRestaurants, getDetails)
    }

    @After
    fun tearDown() {
        RxAndroidPlugins.reset()
    }

    @Test
    fun nearbyTest() {
        viewModel.getNearby(mockLocation)

        viewModel.searchResults.observeForTesting {
            assertEquals(viewModel.searchResults.value, mutableListOf(mockPlace))
        }
    }

    @Test
    fun searchRestaurantsTest() {
        viewModel.searchRestaurant(mockInput, mockLocation)

        viewModel.searchResults.observeForTesting {
            assertEquals(viewModel.searchResults.value, mutableListOf(mockPlace))
        }
    }

    @Test
    fun getDetailsTest() {
        viewModel.getDetails(mockPlaceId)

        viewModel.detailedPlace.observeForTesting {
            assertEquals(viewModel.detailedPlace.value, mockPlace)
        }
    }

    @Test
    fun testSearchQuery() {
        viewModel.setSearchQuery(mockInput)
        assertEquals(viewModel.searchQuery.value, mockInput)
    }
}
