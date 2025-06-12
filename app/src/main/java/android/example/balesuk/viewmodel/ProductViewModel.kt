import android.example.balesuk.data.api.ApiService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import android.example.balesuk.data.repository.ProductRepository
import kotlinx.coroutines.launch
import android.util.Log

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> get() = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getAllProducts(categoryId: Int? = null, search: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.getProducts(categoryId, search)
                if (response.isSuccessful) {
                    _products.value = response.body()?.data
                    _error.value = null
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = errorBody ?: "Unknown error"
                    Log.e("PRODUCTS", "Failed: $errorBody")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("PRODUCTS", "Exception: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
//    fun getProductsByCategory(categorySlug: Int) {
//        viewModelScope.launch {
//            _loading.value = true
//            try {
//                val response = repository.getProductsByCategory(categorySlug)
//                if (response.isSuccessful) {
//                    _products.value = response.body() ?: listOf()
//                    _error.value = null
//                } else {
//                    _error.value = response.message()
//                }
//            } catch (e: Exception) {
//                _error.value = e.message
//            } finally {
//                _loading.value = false
//            }
//        }
//    }


    // Optional: to clear errors
    fun error() {
        _error.value = null
    }
}
