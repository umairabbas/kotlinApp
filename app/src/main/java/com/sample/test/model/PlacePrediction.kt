package com.sample.test.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlacePrediction(val description: String? = "", val place_id: String? = "") : Parcelable