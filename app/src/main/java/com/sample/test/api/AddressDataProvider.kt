package com.sample.test.api

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import com.sample.test.Constants.PLACE_API_KEY
import com.sample.test.Constants.baseUrl
import com.sample.test.model.PlaceDetailRoot
import com.sample.test.model.PlaceResponseRoot
import java.io.Reader

class AddressDataProvider {

    private val placeUrl: String = "$baseUrl/maps/api/place/autocomplete/json"
    private val placeDetailsUrl: String = "$baseUrl/maps/api/place/details/json"


    fun getAddressPredictions(query: String, responseHandler: (result: PlaceResponseRoot) -> Unit?) {
        val params: MutableList<Pair<String, Any?>> = mutableListOf()
        params.add(Pair("input", query))
        params.add(Pair("key", PLACE_API_KEY))
        params.add(Pair("types", "address"))

        placeUrl.httpGet(params)
                .responseObject(AddressDataDeserializer()) { _, response, result ->

                    if (response.httpStatusCode != 200) {
                        throw Exception("Unable to get predictions")
                    }
                    val (data, _) = result
                    responseHandler.invoke(data as PlaceResponseRoot)
                }
    }

    fun getAddressDetails(placeId: String? = "", responseHandler: (result: PlaceDetailRoot) -> Unit?) {
        val params: MutableList<Pair<String, Any?>> = mutableListOf()
        params.add(Pair("placeid", placeId))
        params.add(Pair("key", PLACE_API_KEY))

        placeDetailsUrl.httpGet(params)
                .responseObject(AddressDetailDeserializer()) { _, response, result ->

                    if (response.httpStatusCode != 200) {
                        throw Exception("Unable to get place details")
                    }
                    val (data, _) = result
                    responseHandler.invoke(data as PlaceDetailRoot)
                }
    }

    class AddressDetailDeserializer : ResponseDeserializable<PlaceDetailRoot> {
        override fun deserialize(reader: Reader) = Gson().fromJson(reader, PlaceDetailRoot::class.java)
    }

    class AddressDataDeserializer : ResponseDeserializable<PlaceResponseRoot> {
        override fun deserialize(reader: Reader) = Gson().fromJson(reader, PlaceResponseRoot::class.java)
    }
}