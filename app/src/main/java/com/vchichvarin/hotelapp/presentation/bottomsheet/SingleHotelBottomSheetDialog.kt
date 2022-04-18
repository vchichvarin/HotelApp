package com.vchichvarin.hotelapp.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vchichvarin.hotelapp.App
import com.vchichvarin.hotelapp.data.model.HotelsList
import com.vchichvarin.hotelapp.databinding.CardHotelBinding
import com.vchichvarin.hotelapp.databinding.CardSingleHotelBinding
import com.vchichvarin.hotelapp.di.factory.ViewModelFactory
import com.vchichvarin.hotelapp.presentation.utility.subscribe
import javax.inject.Inject

class SingleHotelBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModelSingleHotel: SingleHotelBottomSheetViewModel

    private var _binding: CardSingleHotelBinding? = null
    private val binding get() = _binding!!

    private lateinit var listBinding: CardHotelBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        App.appComponent.inject(this)

        val single: HotelsList? = arguments?.getParcelable("singleHotel")

        viewModelSingleHotel = ViewModelProvider(this, viewModelFactory).get(SingleHotelBottomSheetViewModel::class.java)

        _binding = CardSingleHotelBinding.inflate(inflater, container, false)

        listBinding = binding.list

        BottomSheetBehavior.from(binding.sheet).apply {
            this.peekHeight = 1200
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        single?.let {
            initListeners(it)
            initInfo(single)
            viewModelSingleHotel.getSingleHotel(it.singleServerHotels.id)
        }

        initObservers()

        return binding.root
    }

    private fun initListeners(single: HotelsList) {
        binding.errorReloadButtonSingle.setOnClickListener {
            viewModelSingleHotel.getSingleHotel(single.singleServerHotels.id)
        }
    }

    private fun initObservers() {
        with(viewModelSingleHotel) {
            imageSingleHotel.subscribe(viewLifecycleOwner, {
                binding.image.setImageBitmap(it)
            })
            statusSingleHotel.subscribe(viewLifecycleOwner, {
                listBinding.name.text = it.serverSingleHotel.name
                listBinding.address.text = it.serverSingleHotel.address
                listBinding.distance.text = it.serverSingleHotel.distance.toString()
                listBinding.rating.rating = it.serverSingleHotel.stars
                listBinding.suits.text = it.suitesList.size.toString()
            })
            errorContainerState.subscribe(viewLifecycleOwner, {
                binding.errorSheet.isVisible = it
            })
            imageContainerState.subscribe(viewLifecycleOwner, {
                binding.image.isVisible = it
            })
            rootContainerState.subscribe(viewLifecycleOwner, {
                listBinding.root.isVisible = it
            })
        }

    }

    private fun initInfo(single: HotelsList) {
        listBinding.name.text = single.singleServerHotels.name
        listBinding.address.text = single.singleServerHotels.address
        listBinding.distance.text = single.singleServerHotels.distance.toString()
        listBinding.rating.rating = single.singleServerHotels.stars
        listBinding.suits.text = single.suitesList.size.toString()
    }
}