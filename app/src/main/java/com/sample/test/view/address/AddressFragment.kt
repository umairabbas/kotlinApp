package com.sample.test.view.address

import android.Manifest
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.sample.test.Constants.SELECTED_ADDRESS
import com.sample.test.R
import com.sample.test.model.AddressTypes
import com.sample.test.model.PlaceDetailRoot
import kotlinx.android.synthetic.main.address_fragment.*

class AddressFragment : Fragment() {

    private lateinit var addressPredictionAdapter: AddressPredictionAdapter
    private var googleApiClient: GoogleApiClient? = null

    private var REQUEST_LOCATION_CODE = 101

    private lateinit var viewModel: AddressViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.address_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddressViewModel::class.java)

        if (googleApiClient == null) {
            context?.let {
                googleApiClient = GoogleApiClient.Builder(it)
                        .addApi(LocationServices.API)
                        .build()
            }
        }
        checkGPSEnabled()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressPredictionAdapter = AddressPredictionAdapter {
            viewModel.fetchAddressDetail(it.place_id) {
                it?.let {
                    textInputAddress.setText(AddressExtraction().extractAddress(it), TextView.BufferType.NORMAL)
                    textInputAddress.setSelection(textInputAddress.text.length)
                    onItemClick(textInputAddress.text.toString())
                }
            }
        }

        buttonLocation.setOnClickListener { getLocationCordinates() }

        recyclerViewAddress.setHasFixedSize(true)
        recyclerViewAddress.layoutManager = LinearLayoutManager(context)
        recyclerViewAddress.adapter = addressPredictionAdapter

        queryAddress()

        viewModel.predictionsLiveDataList.observe(this, Observer
        {
            it?.let {
                addressPredictionAdapter.updateAdapter(it)
            }
        })


    }

    private fun onItemClick(address: String) {
        view?.let {
            val args = Bundle().apply {
                putString(SELECTED_ADDRESS, address)
            }
            Navigation.findNavController(it).navigate(R.id.action_address_to_trip, args, NavOptions.Builder().setPopUpTo(R.id.trip_fragment, true).build())
        }
    }


    private fun queryAddress() {
        textInputAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.fetchAddressPrediction(s.toString()) {
//                    it?.let {
//                        addressPredictionAdapter.updateAdapter(it.predictions)
//                    }
                }
            }
        })
    }

    private fun getLocationCordinates() {
        if (!this.checkGPSEnabled()) {
            return
        }
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                activity?.let { activity ->
                    val location = LocationServices.getFusedLocationProviderClient(activity)
                    if (location != null) {
                        location.lastLocation?.addOnCompleteListener {
                            it.result?.let {
                                val extractAddress = GeocodeAddress(activity).geocodeAddress(location = it)
                                textInputAddress.setText(extractAddress, TextView.BufferType.NORMAL)
                                textInputAddress.setSelection(textInputAddress.text.length)
                            }
                        }
                    } else {
                        startLocationUpdates()
                    }
                }
            } else {
                checkLocationPermission()
            }
        }
    }

    private fun startLocationUpdates() {

        val locationRequest = LocationRequest().apply {
            interval = 5000
            fastestInterval = 2500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        activity?.let { activity ->
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                        locationRequest) {
                    val extractAddress = GeocodeAddress(activity).geocodeAddress(it)
                    textInputAddress.setText(extractAddress, TextView.BufferType.NORMAL)
                    textInputAddress.setSelection(textInputAddress.text.length)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient?.disconnect()
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(getString(R.string.enable_location))
                .setMessage(getString(R.string.enable_location_message))
                .setPositiveButton(getString(R.string.location_setting)) { _, _ ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                .setNegativeButton("Cancel") { _, _ -> }
        dialog.show()
    }

    private fun checkLocationPermission() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    AlertDialog.Builder(context)
                            .setTitle(getString(R.string.location_permission_title))
                            .setMessage(getString(R.string.location_permission_message))
                            .setPositiveButton(getString(R.string.ok_button)) { _, _ ->
                                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                                        REQUEST_LOCATION_CODE)
                            }
                            .create()
                            .show()

                } else requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQUEST_LOCATION_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activity?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getString(R.string.location_granted), Toast.LENGTH_LONG).show()
                        getLocationCordinates()
                    }
                }
            } else {
                Toast.makeText(context, getString(R.string.location_denied), Toast.LENGTH_LONG).show()
            }
            return
        }
    }

}