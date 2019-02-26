package com.sample.test.view.address

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sample.test.R
import com.sample.test.model.PlacePrediction

class AddressPredictionAdapter(private val clickListener: (PlacePrediction) -> Unit) : RecyclerView.Adapter<AddressPredictionViewHolder>() {

    private var placesList = mutableListOf<PlacePrediction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddressPredictionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_address_prediction, parent, false))

    override fun getItemCount() = placesList.size

    override fun onBindViewHolder(holder: AddressPredictionViewHolder, position: Int) {
        holder.updateWithPlaces(placesList[position])
        holder.itemView.setOnClickListener {
            clickListener(placesList[position])
        }
    }

    fun updateAdapter(places: List<PlacePrediction>) {
        placesList.clear()
        placesList.addAll(places)
        notifyDataSetChanged()
    }
}