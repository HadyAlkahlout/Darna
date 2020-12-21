package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemLocationBinding
import com.raiyansoft.darnaapp.model.location.Location

class LocationAdapter(var listener: LocationClick) : RecyclerView.Adapter<LocationAdapter.MyViewHolder>() {

    var data: ArrayList<Location> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemLocationBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_location, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingLocation(data[position])
        binding.imgDelete.setOnClickListener {
            listener.delete(data[position].id)
        }
        binding.tvLocation.setOnClickListener {
            listener.location(data[position].lat, data[position].lng)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemLocationBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingLocation(location: Location) {
            item.location = location
            item.executePendingBindings()
        }
    }

    interface LocationClick {
        fun location(lat: Double, long: Double)
        fun delete(id: Int)
    }
}