package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemInternalCategoryBinding

class InternalCategoriesAdapter(var listener: CategoryCancel) : RecyclerView.Adapter<InternalCategoriesAdapter.MyViewHolder>() {

    var data: ArrayList<String> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemInternalCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_internal_category, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingCategory(data[position])
        binding.imageViewCancel.setOnClickListener {
            listener.cancelClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemInternalCategoryBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingCategory(title: String) {
            item.category = title
            item.executePendingBindings()
        }
    }

    interface CategoryCancel {
        fun cancelClick(position: Int)
    }
}