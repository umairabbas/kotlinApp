package com.sample.test.view.address

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sample.test.api.AddressDataProvider
import com.sample.test.model.PlaceDetailRoot
import com.sample.test.model.PlacePrediction
import com.sample.test.model.PlaceResponseRoot
import org.jetbrains.anko.doAsync

class AddressViewModel : ViewModel() {

    private val addressDataProvider = AddressDataProvider()

    var predictionsLiveDataList: MutableLiveData<ArrayList<PlacePrediction>> = MutableLiveData()


    fun fetchAddressPrediction(query: String, callback: (PlaceResponseRoot?) -> Unit) {
        doAsync {
            try {
                addressDataProvider.getAddressPredictions(query) {
                    predictionsLiveDataList.value?.clear()
                    predictionsLiveDataList.postValue(it.predictions)
                    callback(it)
                }
            } catch (e: Exception) {
                //Show proper error message to user
            }
        }
    }


    fun fetchAddressDetail(placeId: String?, callback: (PlaceDetailRoot?) -> Unit) {
        doAsync {
            try {
                addressDataProvider.getAddressDetails(placeId) {
                    callback(it)
                }
            } catch (e: Exception) {
                //Show proper error message to user
            }
        }
    }

}
