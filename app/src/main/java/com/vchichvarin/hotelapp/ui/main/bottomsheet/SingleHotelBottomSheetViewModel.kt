package com.vchichvarin.hotelapp.ui.main.bottomsheet

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.vchichvarin.hotelapp.BuildConfig
import com.vchichvarin.hotelapp.data.model.CorrectedSingleHotel
import com.vchichvarin.hotelapp.data.model.SingleHotel
import com.vchichvarin.hotelapp.data.repository.HotelRepository
import com.vchichvarin.hotelapp.helper.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleHotelBottomSheetViewModel @Inject constructor(
    private val hotelRepository: HotelRepository
): ViewModel() {

    val statusSingleHotel = MutableLiveData<State>()
    val imageSingleHotel = MutableLiveData<Bitmap>()

    fun getSingleHotel(hotelID: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            hotelRepository.getSingleHotelInfo(hotelID)
                .catch { it.printStackTrace() }
                .collect {
                    when (it) {
                        is State.SuccessLoadedSingleHotel -> {
                            val correctedSingleHotel = correctSingleHotelValue(it.hotel)
                            val image = convertUrlToImage(correctedSingleHotel)
                            statusSingleHotel.postValue(
                                State.SuccessCorrectedSingleHotel(correctedSingleHotel)
                            )
                            imageSingleHotel.postValue(image)
                        }
                        is State.ERROR -> statusSingleHotel.postValue(State.ERROR)
                        else -> statusSingleHotel.postValue(State.ERROR)
                    }
                }
        }
    }

    private fun correctSingleHotelValue(hotel: SingleHotel) : CorrectedSingleHotel {
        val suitesInt = hotel.suites.split(":").filter { it != "" }.map { it.toInt() }
        return CorrectedSingleHotel(hotel, suitesInt)
    }

    private fun convertUrlToImage(hotel: CorrectedSingleHotel) : Bitmap {
        val temp =  Picasso.get()
            .load(BuildConfig.BASE_URL + hotel.singleHotel.image)
            .get()
        val w = temp.width
        val h = temp.height
        return Picasso.get()
            .load(BuildConfig.BASE_URL + hotel.singleHotel.image)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .resize(w-10,h-10)
            .centerCrop()
            .get()
    }


}