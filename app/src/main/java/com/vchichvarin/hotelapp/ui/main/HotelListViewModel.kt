package com.vchichvarin.hotelapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchichvarin.hotelapp.data.model.CorrectedListHotel
import com.vchichvarin.hotelapp.data.model.ListHotel
import com.vchichvarin.hotelapp.data.repository.HotelRepository
import com.vchichvarin.hotelapp.helper.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HotelListViewModel @Inject constructor(
    private val hotelRepository: HotelRepository
): ViewModel() {

    val statusListHotels = MutableLiveData<State>()

    var correctedHotels = ArrayList<CorrectedListHotel>()

    init {
        statusListHotels.postValue(State.LOADING)
    }

    fun getHotels() {
        viewModelScope.launch(Dispatchers.IO) {
            hotelRepository.getHotels()
                .catch { it.printStackTrace() }
                .collect {
                    when (it) {
                        is State.SuccessLoadedListHotel -> {
                            correctedHotels = correctListValues(it.list)
                            statusListHotels.postValue(State.SuccessCorrectedListHotel(correctedHotels))
                        }
                        is State.ERROR -> {
                            statusListHotels.postValue(State.ERROR)
                        }
                        else -> {
                            statusListHotels.postValue(State.ERROR)
                        }
                    }
                }
        }
    }

    private fun correctListValues(list: List<ListHotel>) : ArrayList<CorrectedListHotel>{
        val correctedList = ArrayList<CorrectedListHotel>()
        for (i in list.indices) {
            val suitesInt = list[i].suites.split(":").filter { it != "" }.map { it.toInt() }
            correctedList.add(CorrectedListHotel(list[i].copy(), suitesInt))
        }
        return correctedList
    }
}