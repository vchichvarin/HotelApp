package com.vchichvarin.hotelapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vchichvarin.hotelapp.data.model.CorrectedListHotel
import com.vchichvarin.hotelapp.databinding.CardHotelBinding

class HotelListRecyclerViewAdapter (private var hotelsList: List<CorrectedListHotel>) :
    RecyclerView.Adapter<HotelListRecyclerViewAdapter.HotelListViewHolder>() {

    var openDialog: ((list: CorrectedListHotel) -> Unit)? = null

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

    fun updateList(new: List<CorrectedListHotel>) {
        val hotelDiffUtil = HotelDiffUtil(hotelsList, new)
        val diffResult = DiffUtil.calculateDiff(hotelDiffUtil)
        hotelsList = new
        diffResult.dispatchUpdatesTo(this)
    }

    class HotelListViewHolder(private val itemBinding: CardHotelBinding) :RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var bottomSheetHotel: CorrectedListHotel

        fun bind(
            hotel: CorrectedListHotel,
            openDialog: ((list: CorrectedListHotel) -> Unit)? = null
        ) {
            bottomSheetHotel = hotel
            itemBinding.name.text = hotel.singleHotel.name
            itemBinding.rating.rating = hotel.singleHotel.stars
            itemBinding.address.text = hotel.singleHotel.address
            itemBinding.distance.text = hotel.singleHotel.distance.toString()
            itemBinding.suits.text = hotel.suitesList.size.toString()
            itemView.setOnClickListener {
                openDialog?.invoke(hotel)
            }
        }
    }


    class HotelDiffUtil(
        private val oldList: List<CorrectedListHotel>,
        private val newList: List<CorrectedListHotel>)
        : DiffUtil.Callback () {

        override fun getOldListSize() = oldList.size


        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].singleHotel.id == newList[newItemPosition].singleHotel.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].singleHotel.address == newList[newItemPosition].singleHotel.address &&
            oldList[oldItemPosition].singleHotel.name == newList[newItemPosition].singleHotel.name &&
            oldList[oldItemPosition].singleHotel.distance == newList[newItemPosition].singleHotel.distance
    }
}
