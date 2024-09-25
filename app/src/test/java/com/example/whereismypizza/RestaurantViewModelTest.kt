package com.example.whereismypizza

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.whereismypizza.model.Business
import com.example.whereismypizza.model.Businesses
import com.example.whereismypizza.model.api.ApiService
import com.example.whereismypizza.model.api.YelpAPI
import com.example.whereismypizza.viewmodel.RestaurantViewModel
import com.example.whereismypizza.viewmodel.State
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * unit test, which will execute on the development machine (host).
 */
@ExperimentalCoroutinesApi
class RestaurantViewModelTest {

    // Use different executor
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RestaurantViewModel
    private lateinit var apiService: ApiService
    private lateinit var yelpAPI: YelpAPI

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        yelpAPI = mockk(relaxed = true)
        apiService = mockk {
            every { api } returns yelpAPI
        }

        // Create a ViewModel with the mocked API service
        viewModel = RestaurantViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful fetch with combined businesses`() = runTest() {
        // Mock some Business objects
        val mockBusiness1 = Business(
            id = "1", alias = "beer-place", name = "Beer Place", imageUrl = "url1",
            isClosed = false, url = "url1", reviewCount = 100, categories = listOf(),
            rating = 4.5, coordinates = mockk(), location = mockk(),
            phone = "123456", displayPhone = "123456", distance = 1.0
        )

        val mockBusiness2 = Business(
            id = "2", alias = "pizza-place", name = "Pizza Place", imageUrl = "url2",
            isClosed = false, url = "url2", reviewCount = 50, categories = listOf(),
            rating = 4.0, coordinates = mockk(), location = mockk(),
            phone = "654321", displayPhone = "654321", distance = 2.0
        )

        // Mock the Businesses responses for "beer" and "pizza"
        val beerResponse = Businesses(businesses = listOf(mockBusiness1), total = 1)
        val pizzaResponse = Businesses(businesses = listOf(mockBusiness2), total = 1)

        // Mock the ApiService responses
        coEvery { yelpAPI.getRestaurant(authorization = any(), searchTerm = "beer", location = any()) } returns beerResponse
        coEvery { yelpAPI.getRestaurant(authorization = any(), searchTerm = "pizza", location = any()) } returns pizzaResponse

        // do it!
        viewModel.fetch(Dispatchers.Main)

        // make sure coroutine complete
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.state.value
        assert(result is State.Success)

        val combinedBusinesses = (result as State.Success).businesses

        // combine result
        assert(combinedBusinesses.size == 2) {
            "Expected 2 businesses, but got ${combinedBusinesses.size}"
        }

        // each individual result
        assert(combinedBusinesses[0] == mockBusiness1)
        assert(combinedBusinesses[1] == mockBusiness2)
    }

}