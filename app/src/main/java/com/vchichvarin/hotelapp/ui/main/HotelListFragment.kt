package com.vchichvarin.hotelapp.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vchichvarin.hotelapp.App
import com.vchichvarin.hotelapp.R
import com.vchichvarin.hotelapp.data.model.CorrectedListHotel
import com.vchichvarin.hotelapp.databinding.MainFragmentBinding
import com.vchichvarin.hotelapp.di.factory.ViewModelFactory
import com.vchichvarin.hotelapp.helper.State
import com.vchichvarin.hotelapp.ui.main.adapter.HotelListRecyclerViewAdapter
import com.vchichvarin.hotelapp.ui.main.bottomsheet.SingleHotelBottomSheetDialog
import kotlinx.android.synthetic.main.main_fragment.view.*
import javax.inject.Inject

class HotelListFragment : Fragment() {

    companion object {
        fun newInstance() = HotelListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HotelListViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var hotels = ArrayList<CorrectedListHotel>()
    private var hotelAdapter = HotelListRecyclerViewAdapter(hotels)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)

        App.appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HotelListViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar
        setHasOptionsMenu(true)

        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hotelAdapter
        }

        hotelAdapter.openDialog = { list ->
            val bottomSheetDialog = SingleHotelBottomSheetDialog()
            val bundle = Bundle()
            bundle.putParcelable("singleHotel", list)
            bottomSheetDialog.arguments = bundle
            bottomSheetDialog.show(
                (context as FragmentActivity).supportFragmentManager, bottomSheetDialog.tag
            )
        }

        viewModel.statusListHotels.observe(viewLifecycleOwner, {
            when (it) {
                is State.LOADING -> {
                    binding.recycler.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorContainer.visibility = View.GONE
                }
                is State.ERROR -> {
                    binding.recycler.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.errorContainer.visibility = View.VISIBLE
                }
                is State.SuccessCorrectedListHotel -> {
                    binding.recycler.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.errorContainer.visibility = View.GONE
                    hotels = it.list
                    hotelAdapter.updateList(hotels)
                }
            }
        })

        if (viewModel.correctedHotels.isNotEmpty()) {
            hotels = viewModel.correctedHotels
            hotelAdapter.updateList(hotels)
            binding.recycler.scrollToPosition(0)
        }
        else getHotels()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding.toolbar) {
            inflateMenu(R.menu.user_search)
            setTitle(R.string.app_name)
            setOnMenuItemClickListener { it ->
                when (it.itemId) {
                    R.id.suits_search -> {
                        hotels.sortByDescending { it.suitesList.size }
                        hotelAdapter.notifyDataSetChanged()
                    }

                    R.id.distance_search -> {
                        hotels.sortByDescending { it.singleHotel.distance }
                        hotelAdapter.notifyDataSetChanged()
                    }
                }
                false
            }
        }

        binding.errorContainer.reload_button.setOnClickListener {
            getHotels()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getHotels() {
        viewModel.getHotels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}