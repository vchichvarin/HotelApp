package com.vchichvarin.hotelapp.ui.main.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vchichvarin.hotelapp.App
import com.vchichvarin.hotelapp.data.model.CorrectedListHotel
import com.vchichvarin.hotelapp.databinding.CardHotelBinding
import com.vchichvarin.hotelapp.databinding.CardSingleHotelBinding
import com.vchichvarin.hotelapp.di.factory.ViewModelFactory
import com.vchichvarin.hotelapp.helper.State
import javax.inject.Inject

class SingleHotelBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModelSingleHotel: SingleHotelBottomSheetViewModel

    private var _binding: CardSingleHotelBinding? = null
    private val binding get() = _binding!!

    private lateinit var listBinding: CardHotelBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        App.appComponent.inject(this)

        val singleHotel: CorrectedListHotel? = arguments?.getParcelable("singleHotel")

        viewModelSingleHotel = ViewModelProvider(this, viewModelFactory).get(SingleHotelBottomSheetViewModel::class.java)

        _binding = CardSingleHotelBinding.inflate(inflater, container, false)

        listBinding = binding.list

        BottomSheetBehavior.from(binding.sheet).apply {
            this.peekHeight = 1200
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        listBinding.name.text = singleHotel?.singleHotel?.name
        listBinding.address.text = singleHotel?.singleHotel?.address
        listBinding.distance.text = singleHotel?.singleHotel?.distance.toString()
        listBinding.rating.rating = singleHotel?.singleHotel?.stars!!
        listBinding.suits.text = singleHotel.suitesList.size.toString()

        viewModelSingleHotel.statusSingleHotel.observe(viewLifecycleOwner, {
            when (it) {
                is State.SuccessCorrectedSingleHotel -> {
                    binding.errorSheet.visibility = View.GONE
                    binding.image.visibility = View.VISIBLE
                    listBinding.root.visibility = View.VISIBLE
                    listBinding.name.text = it.hotel.singleHotel.name
                    listBinding.address.text = it.hotel.singleHotel.address
                    listBinding.distance.text = it.hotel.singleHotel.distance.toString()
                    listBinding.rating.rating = it.hotel.singleHotel.stars
                    listBinding.suits.text = it.hotel.suitesList.size.toString()
                }
                else -> {
                    binding.errorSheet.visibility = View.VISIBLE
                    binding.image.visibility = View.GONE
                    listBinding.root.visibility = View.GONE
                    binding.errorReloadButtonSingle.setOnClickListener {
                        viewModelSingleHotel.getSingleHotel(singleHotel.singleHotel.id)
                    }
                }
            }
        })

        viewModelSingleHotel.imageSingleHotel.observe(viewLifecycleOwner, {
            binding.image.setImageBitmap(it)
        })

        viewModelSingleHotel.getSingleHotel(singleHotel.singleHotel.id)

        return binding.root
    }
}