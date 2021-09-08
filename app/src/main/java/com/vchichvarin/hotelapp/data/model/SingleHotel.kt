package com.vchichvarin.hotelapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SingleHotel(
    val id: Int,
    val name : String,
    val address: String,
    val stars: Float,
    val distance: Float,
    val image: String,
    @SerializedName ("suites_availability")
    val suites: String,
    val lat: Double,
    val lon: Double
) : Parcelable

@Parcelize
data class CorrectedSingleHotel (
    val singleHotel: SingleHotel,
    val suitesList: List<Int>
) : Parcelable