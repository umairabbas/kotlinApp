package com.sample.test.view.address

import android.support.v7.widget.RecyclerView
import android.view.View
import com.sample.test.model.PlacePrediction
import kotlinx.android.synthetic.main.item_list_address_prediction.view.*

class AddressPredictionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithPlaces(place: PlacePrediction) {
        itemView.textAddress.text = place.description
    }
}