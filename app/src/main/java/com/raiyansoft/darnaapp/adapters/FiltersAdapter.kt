package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemFilterBinding
import com.raiyansoft.darnaapp.databinding.ItemInternalCategoryBinding

class FiltersAdapter(var listener: FilterCancel) : RecyclerView.Adapter<FiltersAdapter.MyViewHolder>() {

    var data: ArrayList<String> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemFilterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_filter, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingFilter(data[position])
        binding.imageViewCancel.setOnClickListener {
            listener.cancelClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemFilterBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingFilter(title: String) {
            item.filter = title
            item.executePendingBindings()
        }
    }

    interface FilterCancel {
        fun cancelClick(position: Int)
    }
}