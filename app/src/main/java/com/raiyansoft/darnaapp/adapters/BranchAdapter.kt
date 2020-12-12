package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemBranchBinding
import com.raiyansoft.darnaapp.model.branch.Branch

class BranchAdapter(var listener: BranchClick) : RecyclerView.Adapter<BranchAdapter.MyViewHolder>() {

    var data: ArrayList<Branch> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemBranchBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_branch, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingBranch(data[position])
        binding.textViewLocation.setOnClickListener {
            listener.locationClick(data[position].lat, data[position].lng)
        }
        binding.imageViewDelete.setOnClickListener {
            listener.deleteClick(data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemBranchBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingBranch(branch: Branch) {
            item.branch = branch
            item.executePendingBindings()
        }
    }

    interface BranchClick {
        fun locationClick(lat: Double, long: Double)
        fun deleteClick(id: Int)
    }
}