package com.multibahana.dummyjsonapp.data.repository

import com.multibahana.dummyjsonapp.data.model.ProductDto
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse
import com.multibahana.dummyjsonapp.data.remote.api.ProductService
import com.multibahana.dummyjsonapp.domain.repository.ProductRepository
import jakarta.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProducts(
        limit: Int?,
        skip: Int?,
        select: String?,
        sortBy: String?,
        order: String?
    ): Result<ProductResponse> {
        return try {
            val response = productService.getProducts(
                limit = limit, skip = skip,
                select = select, sortBy = sortBy, order = order
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception(response.errorBody().toString()))
            } else {
                Result.failure(Exception(response.code().toString()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<ProductDto> {
        TODO("Not yet implemented")
    }

    override suspend fun searchProducts(
        query: String,
        limit: Int?,
        skip: Int?,
        select: String?,
        sortBy: String?,
        order: String?
    ): Result<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addProduct(product: ProductDto): Result<ProductDto> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(
        id: Int,
        product: ProductDto
    ): Result<ProductDto> {
        TODO("Not yet implemented")
    }

    override suspend fun patchProduct(
        id: Int,
        updates: Map<String, Any>
    ): Result<ProductDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: Int): Result<ProductDto> {
        TODO("Not yet implemented")
    }
}