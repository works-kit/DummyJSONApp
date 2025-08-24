package com.multibahana.dummyjsonapp.presentation.home

import com.multibahana.dummyjsonapp.data.model.ProductDto
import com.multibahana.dummyjsonapp.data.remote.api.ProductResponse

data class ProductListState(
    val isLogout: Boolean = false,
    val isLoading: Boolean = false,
    val productList: ProductResponse? = null,
    val error: String? = null
)

data class ProductState(
    val isLogout: Boolean = false,
    val isLoading: Boolean = false,
    val product: ProductDto? = null,
    val error: String? = null
)