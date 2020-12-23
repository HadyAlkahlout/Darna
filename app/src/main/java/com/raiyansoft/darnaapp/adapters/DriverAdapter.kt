package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemDriverBinding
import com.raiyansoft.darnaapp.model.driver.Driver

class DriverAdapter(var isSelect: Boolean, var listener: DriverClick) : RecyclerView.Adapter<DriverAdapter.MyViewHolder>() {

    var data: ArrayList<Driver> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemDriverBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_driver, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingDriver(data[position])
        binding.imgDelete.setOnClickListener {
            listener.deleteDriver(data[position].id)
        }
        binding.isSelect = isSelect
        binding.rbSelect.setOnCheckedChangeListener { _, _ ->
            for (i in 0 until data.size) {
                if (data[i].isSelect) {
                    data[i].isSelect = false
                }
            }
            data[position].isSelect = true
            notifyDataSetChanged()
            listener.assignDriver(data[position].id)
        }
        binding.rbSelect.isChecked = data[position].isSelect

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemDriverBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingDriver(driver: Driver) {
            item.driver = driver
            item.executePendingBindings()
        }
    }

    interface DriverClick {
        fun deleteDriver(id: Int)
        fun assignDriver(id: Int)
    }
}