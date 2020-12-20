package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemReportOrderBinding
import com.raiyansoft.darnaapp.model.orders.Order

class ReportOrderAdapter: RecyclerView.Adapter<ReportOrderAdapter.MyViewHolder>() {

    var data: ArrayList<Order> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemReportOrderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_report_order, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingOrder(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemReportOrderBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingOrder(order: Order) {
            item.order = order
            item.executePendingBindings()
        }
    }
}