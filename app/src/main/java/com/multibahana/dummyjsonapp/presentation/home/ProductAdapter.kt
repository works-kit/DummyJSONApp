package com.multibahana.dummyjsonapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.data.model.ProductDto
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse
import com.multibahana.dummyjsonapp.databinding.RvProductItenBinding

class ProductAdapter(private val productList: ProductResponse) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val binding =
            RvProductItenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList.products[position]

        holder.binding.apply {
            tvTitle.text = product.title ?: "-"

            val priceText = product.price?.let {
                "$ %,d".format(it.toLong())
            } ?: "$ 0"
            tvPrice.text = priceText

            tvStock.text = product.stock?.toString() ?: "0"

            Glide.with(ivThumbnail.context)
                .load(product.thumbnail)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivThumbnail)
        }
    }



    override fun getItemCount(): Int = productList.products.size

    inner class ProductViewHolder(val binding: RvProductItenBinding) :
        RecyclerView.ViewHolder(binding.root)
}