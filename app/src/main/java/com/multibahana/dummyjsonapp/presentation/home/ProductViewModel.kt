package com.multibahana.dummyjsonapp.presentation.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.multibahana.dummyjsonapp.domain.usecase.ProductUseCase
import com.multibahana.dummyjsonapp.presentation.auth.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _productListState = MutableStateFlow(ProductListState())
    val productListState: StateFlow<ProductListState> = _productListState

    private val _productState = MutableStateFlow(ProductState())
    val productState: StateFlow<ProductState> = _productState

    fun getProducts(
        limit: Int? = null,
        skip: Int? = null,
        select: String? = null,
        sortBy: String? = null,
        order: String? = null
    ) {

         viewModelScope.launch {
            _productListState.value = ProductListState(isLoading = true)

            try {
                val result = productUseCase.getProducts(limit, skip, select, sortBy, order)
                val products = result.getOrNull()

                products?.let {
                    _productListState.value = ProductListState(
                        productList = products,
                        isLoading = false,
                        isLogout = false
                    )
                }
            } catch (e: Exception) {
                _productListState.value = ProductListState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _productState.value = ProductState(isLoading = true)
            try {
                val result = productUseCase.getProductById(id)
                val product = result.getOrNull()

                product?.let {
                    _productState.value = ProductState(
                        product = product,
                        isLoading = false,
                        isLogout = false
                    )
                }
            } catch (e: Exception) {
                _productState.value = ProductState(error = e.message ?: "Unknown error")
            }
        }
    }
}
