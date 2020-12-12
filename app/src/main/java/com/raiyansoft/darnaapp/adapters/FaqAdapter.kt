package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemFaqBinding
import com.raiyansoft.darnaapp.model.setting.faq.Question

class FaqAdapter : RecyclerView.Adapter<FaqAdapter.MyViewHolder>() {

    var data: ArrayList<Question> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemFaqBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_faq, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingFaq(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemFaqBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingFaq(faq: Question) {
            item.question = faq
            item.executePendingBindings()
        }
    }
}