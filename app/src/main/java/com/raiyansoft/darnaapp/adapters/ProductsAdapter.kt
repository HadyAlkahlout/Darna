package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemProductBinding
import com.raiyansoft.darnaapp.model.product.Product

class ProductsAdapter(var isShow: Boolean, var data: ArrayList<Product>, var listener: ProductClick) : RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_product, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingProduct(data[position])
        binding.isSold = data[position].quantity == 0
        binding.isShow = isShow
        binding.cardProduct.setOnClickListener {
            listener.productClick(data[position].id)
        }
        binding.imageViewDelete.setOnClickListener {
            listener.deleteClick(data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemProductBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingProduct(product: Product) {
            item.product = product
            item.textViewTime.text = product.time.toString()
            item.executePendingBindings()
        }
    }

    interface ProductClick {
        fun productClick(id: Int)
        fun deleteClick(id: Int)
    }
}