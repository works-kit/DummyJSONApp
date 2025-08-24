package com.multibahana.dummyjsonapp.domain.repository

import com.multibahana.dummyjsonapp.data.model.ProductDto
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse

interface ProductRepository {

    suspend fun getProducts(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Result<ProductResponse>

    suspend fun getProductById(id: Int): Result<ProductDto>

    suspend fun searchProducts(
        query: String,
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Result<ProductResponse>

    suspend fun addProduct(product: ProductDto): Result<ProductDto>

    suspend fun updateProduct(id: Int, product: ProductDto): Result<ProductDto>

    suspend fun patchProduct(id: Int, updates: Map<String, Any>): Result<ProductDto>

    suspend fun deleteProduct(id: Int): Result<ProductDto>
}
