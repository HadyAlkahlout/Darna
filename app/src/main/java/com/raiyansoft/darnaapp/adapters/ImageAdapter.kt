package com.raiyansoft.darnaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.ItemImageBinding
import com.raiyansoft.darnaapp.model.productDetails.Image

class ImageAdapter(val listener: ImageClick) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    var data: ArrayList<Image> = ArrayList()
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var binding: ItemImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_image, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bingImage(data[position])
        binding.imageDelete.setOnClickListener {
            listener.deleteImage(position, data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var item: ItemImageBinding) : RecyclerView.ViewHolder(item.root) {
        fun bingImage(image: Image) {
            item.image = image
            item.executePendingBindings()
        }
    }

    interface ImageClick {
        fun deleteImage(position: Int, id: Int)
    }
}