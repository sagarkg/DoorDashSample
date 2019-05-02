package com.sagarganatra.doordashsample.repository

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.sagarganatra.doordashsample.api.RestaurantsApi
import com.sagarganatra.doordashsample.models.Restaurant
import com.sagarganatra.doordashsample.utils.BASE_URL
import com.sagarganatra.doordashsample.utils.getRestaurantListSampleResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantRespositoryImplTest {

    private val mockWebServer = MockWebServer()

    // Repository Implementation to be tested
    private lateinit var repo: RestaurantsRepositoryImpl

    private val testObserver: TestObserver<List<Restaurant>> = TestObserver.create()

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        repo = RestaurantsRepositoryImpl(retrofit.create(RestaurantsApi::class.java))
    }

    @Test
    fun testValidUrlAndMethodCall() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("")
        mockWebServer.enqueue(mockResponse)

        repo.getRestaurantsByLatLong(
            "a",
            "b"
        ).subscribe(testObserver)

        testObserver.awaitTerminalEvent()

        val request = mockWebServer.takeRequest()
        assert(request.path == "/restaurant?lat=a&lng=b")
        assert(request.method == "GET")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}