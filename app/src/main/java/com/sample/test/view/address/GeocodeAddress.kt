package com.sample.test.view.address

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.util.*

class GeocodeAddress(context: Context) {

     private var geocoder: Geocoder

    init {
        geocoder = Geocoder(context, Locale.getDefault())
    }

    fun setGeoCoder(geocoder: Geocoder) {
        this.geocoder = geocoder
    }

    fun geocodeAddress(location: Location): String {
        val addressList = geocoder.getFromLocation(
                location.latitude, location.longitude, 1)
        return extractAddress(addressList)
    }

    private fun extractAddress(addressList: List<Address>): String {
        return if (addressList.isNotEmpty()) {
            val address = addressList[0]
            val sb = StringBuilder()
            sb.append(address.thoroughfare)
            if(!address.featureName.isNullOrEmpty()){
                sb.append(" ").append(address.featureName).append(", ")
            }else{
                sb.append(", ")
            }
            sb.append(address.postalCode).append(" ")
            sb.append(address.locality)
            sb.toString()
        } else {
            ""
        }
    }
}