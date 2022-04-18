package com.vchichvarin.hotelapp.presentation.bottomsheet

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.vchichvarin.hotelapp.BuildConfig
import com.vchichvarin.hotelapp.data.model.SingleHotel
import com.vchichvarin.hotelapp.domain.interactor.HotelInteractor
import com.vchichvarin.hotelapp.domain.utility.InteractorState
import com.vchichvarin.hotelapp.presentation.utility.BaseViewModelLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleHotelBottomSheetViewModel @Inject constructor(
    private val hotelInteractor: HotelInteractor
): ViewModel() {

    val statusSingleHotel = BaseViewModelLiveData<SingleHotel>()
    val imageSingleHotel = BaseViewModelLiveData<Bitmap>()
    val errorContainerState = BaseViewModelLiveData<Boolean>()
    val rootContainerState = BaseViewModelLiveData<Boolean>()
    val imageContainerState = BaseViewModelLiveData<Boolean>()

    fun getSingleHotel(hotelID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            hotelInteractor.getSingleHotelInfo(hotelID)
                .catch { it.printStackTrace() }
                .collect {
                    when (it) {
                        is InteractorState.SuccessInteractorStateSingleHotel -> {
                            val image = convertUrlToImage(it.hotel)
                            statusSingleHotel.setValue(it.hotel)
                            imageSingleHotel.setValue(image)
                            errorContainerState.setValue(false)
                            rootContainerState.setValue(true)
                            imageContainerState.setValue(true)
                        }
                        is InteractorState.ERROR -> {
                            rootContainerState.setValue(false)
                            errorContainerState.setValue(true)
                            imageContainerState.setValue(false)
                        }
                    }
                }
        }
    }

    private fun convertUrlToImage(hotel: SingleHotel) : Bitmap {
        val temp =  Picasso.get()
            .load(BuildConfig.BASE_URL + hotel.serverSingleHotel.image)
            .get()
        val w = temp.width
        val h = temp.height
        return Picasso.get()
            .load(BuildConfig.BASE_URL + hotel.serverSingleHotel.image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .resize(w-10,h-10)
            .centerCrop()
            .get()
    }


}