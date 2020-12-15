package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemOptionBinding
import com.raiyansoft.darnaapp.model.productDetails.Option

class OptionAdapter(var listener: OptionCancel) : RecyclerView.Adapter<OptionAdapter.MyViewHolder>() {

    var data: ArrayList<Option> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemOptionBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_option, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingOption(data[position])
        binding.imageViewDelete.setOnClickListener {
            listener.optionCancel(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemOptionBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingOption(option: Option) {
            item.option = option
            item.executePendingBindings()
        }
    }

    interface OptionCancel {
        fun optionCancel(position: Int)
    }
}