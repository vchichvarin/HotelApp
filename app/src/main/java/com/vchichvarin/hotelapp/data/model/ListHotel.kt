package com.vchichvarin.hotelapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListHotel(
    val id: Int,
    val name: String,
    val address: String,
    val stars: Float,
    val distance: Float,
    @SerializedName("suites_availability")
    val suites: String
) : Parcelable

@Parcelize
data class CorrectedListHotel(
    var singleHotel: ListHotel,
    var suitesList: List<Int>
) : Parcelable