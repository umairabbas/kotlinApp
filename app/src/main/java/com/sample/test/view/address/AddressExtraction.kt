package com.sample.test.view.address

import com.sample.test.model.AddressTypes
import com.sample.test.model.PlaceDetailRoot

class AddressExtraction{

    fun extractAddress(places: PlaceDetailRoot): String {
        var postal: String? = null
        var city: String? = null
        var route: String? = null
        var street: String? = null

        places.result.address_components.forEach { address ->
            address.types.forEach {
                when (it.toString()) {
                    AddressTypes.city -> {
                        city = address.long_name
                    }
                    AddressTypes.postal -> {
                        postal = address.long_name
                    }
                    AddressTypes.route -> {
                        route = address.long_name
                    }
                    AddressTypes.street -> {
                        street = address.long_name
                    }
                }
            }
        }
        val sb = StringBuilder()
        route?.let { sb.append(it) }
        street?.let { sb.append(" ").append(it) }
        postal?.let { sb.append(", ").append(it) }
        city?.let { sb.append(" ").append(it) }
        return sb.toString()
    }
}