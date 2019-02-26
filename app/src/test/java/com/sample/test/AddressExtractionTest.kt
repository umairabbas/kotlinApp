package com.sample.test

import com.sample.test.model.AddressTypes
import com.sample.test.model.PlaceDetailRoot
import com.sample.test.model.PlaceInnerAddressComponents
import com.sample.test.view.address.AddressExtraction
import org.junit.Assert
import org.junit.Test

class AddressExtractionTest {

    @Test
    fun addresseEmptyTest() {
        Assert.assertEquals("", AddressExtraction().extractAddress(PlaceDetailRoot()))
    }

    @Test
    fun addresseTest() {

        val placeDetailRoot = PlaceDetailRoot()
        val city = arrayListOf<Any?>()
        city.add(AddressTypes.city)
        placeDetailRoot.result.address_components.add(PlaceInnerAddressComponents("Aachen", city))

        val postal = arrayListOf<Any?>()
        postal.add(AddressTypes.postal)
        placeDetailRoot.result.address_components.add(PlaceInnerAddressComponents("52074", postal))

        val route = arrayListOf<Any?>()
        route.add(AddressTypes.route)
        placeDetailRoot.result.address_components.add(PlaceInnerAddressComponents("Melatener Str.", route))

        val street = arrayListOf<Any?>()
        street.add(AddressTypes.street)
        placeDetailRoot.result.address_components.add(PlaceInnerAddressComponents("96", street))

        Assert.assertEquals("Melatener Str. 96, 52074 Aachen", AddressExtraction().extractAddress(placeDetailRoot))
    }

}