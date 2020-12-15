package com.raiyansoft.darnaapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemOrderBinding
import com.raiyansoft.darnaapp.model.orders.Order

class OrderAdapter(var listener: OrderClick) : RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    var data: ArrayList<Order> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemOrderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_order, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingOrder(data[position])
        when(data[position].status_id){
            1 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
            }
            2 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
            }
            3 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
            }
            4 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
            }
            5 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#29B44E"))
            }
            6 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
            }
            7 -> {
                binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
            }
        }
        binding.cardOrder.setOnClickListener {
            listener.orderClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemOrderBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingOrder(order: Order) {
            item.order = order
            item.executePendingBindings()
        }
    }

    interface OrderClick {
        fun orderClick(id: Int)
    }
}