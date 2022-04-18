package com.vchichvarin.hotelapp.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vchichvarin.hotelapp.App
import com.vchichvarin.hotelapp.R
import com.vchichvarin.hotelapp.databinding.MainFragmentBinding
import com.vchichvarin.hotelapp.di.factory.ViewModelFactory
import com.vchichvarin.hotelapp.presentation.adapter.HotelListRecyclerViewAdapter
import com.vchichvarin.hotelapp.presentation.bottomsheet.SingleHotelBottomSheetDialog
import com.vchichvarin.hotelapp.presentation.utility.subscribe
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

    private var hotelAdapter = HotelListRecyclerViewAdapter()

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

        initDialogs(hotelAdapter)

        initObservers()

        return binding.root
    }

    private fun initDialogs(hotelAdapter: HotelListRecyclerViewAdapter) {
        hotelAdapter.openDialog = { list ->
            val bottomSheetDialog = SingleHotelBottomSheetDialog()
            val bundle = Bundle()
            bundle.putParcelable("singleHotel", list)
            bottomSheetDialog.arguments = bundle
            bottomSheetDialog.show(
                (context as FragmentActivity).supportFragmentManager, bottomSheetDialog.tag
            )
        }
    }

    private fun initObservers() {
        viewModel.loaderState.subscribe(viewLifecycleOwner, {
            binding.progressBar.isVisible = it
        })

        viewModel.hotelsListState.subscribe(viewLifecycleOwner, {
            hotelAdapter.updateList(it)
        })

        viewModel.errorContainerState.subscribe(viewLifecycleOwner, {
            binding.errorContainer.isVisible = it
        })

        viewModel.recyclerViewState.subscribe(viewLifecycleOwner, {
            binding.recycler.isVisible = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding.toolbar) {
            inflateMenu(R.menu.user_search)
            setTitle(R.string.app_name)
            setOnMenuItemClickListener { it ->
                when (it.itemId) {
                    R.id.suits_search -> { viewModel.sortHotelsListSuites() }
                    R.id.distance_search -> { viewModel.sortHotelsListDistance() }
                }
                false
            }
        }

        binding.errorContainer.reload_button.setOnClickListener {
            viewModel.getHotelsList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}