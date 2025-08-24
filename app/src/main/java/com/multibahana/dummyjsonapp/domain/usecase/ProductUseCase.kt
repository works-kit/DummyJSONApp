package com.multibahana.dummyjsonapp.domain.usecase

import com.multibahana.dummyjsonapp.data.model.ProductDto
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse
import com.multibahana.dummyjsonapp.domain.repository.ProductRepository
import jakarta.inject.Inject


class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend fun getProducts(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Result<ProductResponse> {
        return productRepository.getProducts(limit, skip, select, sortBy, order)
    }

    suspend fun getProductById(id: Int): Result<ProductDto> {
        return productRepository.getProductById(id)
    }

    suspend fun searchProducts(
        query: String,
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ): Result<ProductResponse> {
        return productRepository.searchProducts(query, limit, skip, select, sortBy, order)
    }

    suspend fun addProduct(product: ProductDto): Result<ProductDto> {
        return productRepository.addProduct(product)
    }

    suspend fun updateProduct(id: Int, product: ProductDto): Result<ProductDto> {
        return productRepository.updateProduct(id, product)
    }

    suspend fun patchProduct(id: Int, updates: Map<String, Any>): Result<ProductDto> {
        return productRepository.patchProduct(id, updates)
    }

    suspend fun deleteProduct(id: Int): Result<ProductDto> {
        return productRepository.deleteProduct(id)
    }

}
