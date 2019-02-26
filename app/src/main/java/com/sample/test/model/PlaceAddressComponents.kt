package com.sample.test.model

class PlaceAddressComponents(
        val name: String? = "",
        val address_components: ArrayList<PlaceInnerAddressComponents> = ArrayList()
)