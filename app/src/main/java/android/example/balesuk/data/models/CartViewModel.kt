package android.example.balesuk.ui.viewmodel

import android.example.balesuk.data.CartItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun addItem(item: CartItem) {
        val currentList = _cartItems.value.orEmpty()
        val existingItem = currentList.find { it.productId == item.productId }

        val updatedList = if (existingItem != null) {
            currentList.map {
                if (it.productId == item.productId)
                    it.copy(quantity = it.quantity + item.quantity)
                else it
            }
        } else {
            currentList + item
        }

        _cartItems.value = updatedList
    }


    fun removeItem(itemId: String) {
        val updatedList = _cartItems.value.orEmpty().toMutableList()
        updatedList.removeAll { it.productId == itemId }
        _cartItems.value = updatedList
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        val updatedList = _cartItems.value.orEmpty().map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
        _cartItems.value = updatedList
    }


    fun clearCart() {
        _cartItems.value = mutableListOf()
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.orEmpty().sumOf { it.price * it.quantity }
    }
}
