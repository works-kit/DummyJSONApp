package com.multibahana.dummyjsonapp.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multibahana.dummyjsonapp.R
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse
import com.multibahana.dummyjsonapp.databinding.RvProductItenBinding

class ProductAdapter(private var productList: ProductResponse) :
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

    fun updateData(products: ProductResponse) {
        val diffCallback = ProductDiffCallback(productList, products)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        productList = products
        diffResult.dispatchUpdatesTo(this)
    }

    class ProductDiffCallback(
        private val oldList: ProductResponse,
        private val newList: ProductResponse
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.products.size
        override fun getNewListSize(): Int = newList.products.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // bandingkan berdasarkan id unik
            return oldList.products[oldItemPosition].id == newList.products[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // bandingkan isi item (jika data class bisa langsung pakai == )
            return oldList.products[oldItemPosition] == newList.products[newItemPosition]
        }
    }
}