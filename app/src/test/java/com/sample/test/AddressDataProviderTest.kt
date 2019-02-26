package com.sample.test

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.sample.test.Constants.baseUrl
import com.sample.test.api.AddressDataProvider
import com.sample.test.model.PlaceResponseRoot
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class AddressDataProviderTest : BaseTestCase() {

    private var query = "melatener"

    init {
        FuelManager.instance.apply {
            basePath = baseUrl
            callbackExecutor = Executor(Runnable::run)
        }
    }

    @Before
    fun setUp() {
        lock = CountDownLatch(1)
    }

    @Test
    fun httpGetPlaceResultObject() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null
        val params: MutableList<Pair<String, Any?>> = mutableListOf()
        params.add(Pair("key", Constants.PLACE_API_KEY))
        params.add(Pair("types", "address"))
        params.add(Pair("input", query))

        Fuel.get("maps/api/place/autocomplete/json", params).responseObject(AddressDataProvider.AddressDataDeserializer()) { req, res, result ->
            val (d, e) = result
            request = req
            response = res
            data = d
            error = e

            lock.countDown()
        }

        await()

        Assert.assertThat(request, CoreMatchers.notNullValue())
        Assert.assertThat(response, CoreMatchers.notNullValue())
        Assert.assertThat(error, CoreMatchers.nullValue())
        Assert.assertThat(data, CoreMatchers.notNullValue())
        Assert.assertThat(data as PlaceResponseRoot, CoreMatchers.isA(PlaceResponseRoot::class.java))

        val placesList: PlaceResponseRoot = data as PlaceResponseRoot
        assert(placesList.predictions.size > 0)

        val statusCode = HttpURLConnection.HTTP_OK
        Assert.assertThat(response?.httpStatusCode, CoreMatchers.`is`(statusCode))
    }

}

@RunWith(RobolectricTestRunner::class)
abstract class BaseTestCase {

    private val DEFAULT_TIMEOUT = 15L

    lateinit var lock: CountDownLatch

    fun await(seconds: Long = DEFAULT_TIMEOUT) {
        lock.await(seconds, TimeUnit.SECONDS)
    }

}