package com.vchichvarin.hotelapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vchichvarin.hotelapp.data.model.HotelsList
import com.vchichvarin.hotelapp.databinding.CardHotelBinding

class HotelListRecyclerViewAdapter (
    private var hotelsList: List<HotelsList> = ArrayList()
) : RecyclerView.Adapter<HotelListRecyclerViewAdapter.HotelListViewHolder>() {

    var openDialog: ((list: HotelsList) -> Unit)? = null

    private lateinit var binding: CardHotelBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelListViewHolder {
        binding = CardHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotelListViewHolder, position: Int) {
        holder.bind(hotelsList[position], openDialog)
    }

    override fun getItemCount(): Int {
        return hotelsList.size
    }

    fun updateList(newHotelsList: List<HotelsList>) {
        val hotelDiffUtil = HotelDiffUtil(hotelsList, newHotelsList)
        val diffResult = DiffUtil.calculateDiff(hotelDiffUtil)
        hotelsList = newHotelsList
        diffResult.dispatchUpdatesTo(this)
    }

    class HotelListViewHolder(private val itemBinding: CardHotelBinding) :RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var bottomSheet: HotelsList

        fun bind(
            hotel: HotelsList,
            openDialog: ((list: HotelsList) -> Unit)? = null
        ) {
            bottomSheet = hotel
            itemBinding.name.text = hotel.singleServerHotels.name
            itemBinding.rating.rating = hotel.singleServerHotels.stars
            itemBinding.address.text = hotel.singleServerHotels.address
            itemBinding.distance.text = hotel.singleServerHotels.distance.toString()
            itemBinding.suits.text = hotel.suitesList.size.toString()
            itemView.setOnClickListener {
                openDialog?.invoke(hotel)
            }
        }
    }


    class HotelDiffUtil(
        private val oldList: List<HotelsList>,
        private val newList: List<HotelsList>)
        : DiffUtil.Callback () {

        override fun getOldListSize() = oldList.size


        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].singleServerHotels.id == newList[newItemPosition].singleServerHotels.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].singleServerHotels.address == newList[newItemPosition].singleServerHotels.address &&
            oldList[oldItemPosition].singleServerHotels.name == newList[newItemPosition].singleServerHotels.name &&
            oldList[oldItemPosition].singleServerHotels.distance == newList[newItemPosition].singleServerHotels.distance
    }
}
