package com.sample.test.view.trip

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.sample.test.Constants.SELECTED_ADDRESS
import com.sample.test.R
import kotlinx.android.synthetic.main.trip_fragment.*
import android.widget.Toast


class TripFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.trip_fragment, container, false)
    }

    private fun getSelectedAddress() = arguments?.getString(SELECTED_ADDRESS) ?: ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (getSelectedAddress().isNotEmpty()) {
            eTTo.text = Editable.Factory.getInstance().newEditable(getSelectedAddress())
        }

        eTTo.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_trip_to_address)
        }

        eTTo.setOnTouchListener { _, event ->

            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= (eTTo.right - eTTo.compoundDrawables[2].bounds.width())){
                    swapFromToAddress()
                    true
                } else
                false
            } else false
        }
    }

    private fun swapFromToAddress(){
        val temp = eTTo.text.toString()
        eTTo.text = Editable.Factory.getInstance().newEditable(eTFrom.text.toString())
        eTFrom.text = Editable.Factory.getInstance().newEditable(temp)
    }

}