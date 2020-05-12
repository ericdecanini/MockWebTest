package com.ericdecanini.mockwebtest

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class MockWebUnitTests {

    private val server: MockWebServer = MockWebServer()
    private val MOCK_WEBSERVER_PORT = 8000

    lateinit var placeholderApi: PlaceholderApi
    lateinit var jsonRepository: JsonRepository

    @Before
    fun init() {
        server.start(MOCK_WEBSERVER_PORT)

        placeholderApi = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(PlaceholderApi::class.java)

        jsonRepository = JsonRepository(placeholderApi)
    }

    @After
    fun shutdown() {
        server.shutdown()
    }

    @Test
    fun `JsonPlaceholder APIs parse correctly`() {
        server.apply {
            enqueue(MockResponse().setBody(MockResponseFileReader("jsonplaceholder_success.json").content))
        }
        jsonRepository.observePosts()
            .test()
            .awaitDone(3, TimeUnit.SECONDS)
            .assertComplete()
            .assertValueCount(1)
            .assertValue { it.size == 2 }
            .assertNoErrors()
    }

}