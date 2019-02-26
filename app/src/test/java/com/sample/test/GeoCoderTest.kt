package com.sample.test

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.sample.test.view.address.GeocodeAddress
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.*

class GeoCoderTest {

    @Mock
    lateinit var location: Location

    @Mock
    lateinit var geocoder: Geocoder

    @Mock
    var address: Address = Address(
            Locale.GERMANY
    )

    private val context: Context = mock(Context::class.java)

    private val expectedResult = "STREET HOUSE_NUM, ZIPCODE CITYNAME"

    private val expectedResultWithFeatureEmpty = "STREET, ZIPCODE CITYNAME"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun addressExtractionTest() {

        Mockito.`when`(address.countryName)
                .thenReturn("COUNTRY_NAME")
        Mockito.`when`(address.countryCode)
                .thenReturn("COUNTRY_CODE")
        Mockito.`when`(address.postalCode)
                .thenReturn("ZIPCODE")
        Mockito.`when`(address.thoroughfare)
                .thenReturn("STREET")
        Mockito.`when`(address.featureName)
                .thenReturn("HOUSE_NUM")
        Mockito.`when`(address.locality)
                .thenReturn("CITYNAME")
        Mockito.`when`(location.latitude)
                .thenReturn(50.7774052)
        Mockito.`when`(location.longitude)
                .thenReturn(6.0602235)

        val addresses = ArrayList<Address>()

        addresses.add(address)

        Mockito.`when`(geocoder.getFromLocation(location.latitude, location.longitude, 1))
                .thenReturn(addresses)

        val geocodeAddress = GeocodeAddress(context)

        geocodeAddress.setGeoCoder(geocoder)

        val result = geocodeAddress.geocodeAddress(location)

        assertEquals(result, expectedResult)

    }

    @Test
    fun addressExtractionTestWhenFeatureIsEmpty() {

        Mockito.`when`(address.countryName)
                .thenReturn("COUNTRY_NAME")
        Mockito.`when`(address.countryCode)
                .thenReturn("COUNTRY_CODE")
        Mockito.`when`(address.postalCode)
                .thenReturn("ZIPCODE")
        Mockito.`when`(address.thoroughfare)
                .thenReturn("STREET")
        Mockito.`when`(address.featureName)
                .thenReturn("")
        Mockito.`when`(address.locality)
                .thenReturn("CITYNAME")
        Mockito.`when`(location.latitude)
                .thenReturn(50.7774052)
        Mockito.`when`(location.longitude)
                .thenReturn(6.0602235)

        val addresses = ArrayList<Address>()

        addresses.add(address)

        Mockito.`when`(geocoder.getFromLocation(location.latitude, location.longitude, 1))
                .thenReturn(addresses)

        val geocodeAddress = GeocodeAddress(context)

        geocodeAddress.setGeoCoder(geocoder)

        val result = geocodeAddress.geocodeAddress(location)

        assertEquals(result, expectedResultWithFeatureEmpty)

    }
}